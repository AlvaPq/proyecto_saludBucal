
import { useState } from 'react'

export default function HistorialAtenciones({ volver }) {
  const [tipoBusqueda, setTipoBusqueda] = useState('dni')
  const [busqueda, setBusqueda] = useState('')
  const [atenciones, setAtenciones] = useState([])
  const [pacienteEncontrado, setPacienteEncontrado] = useState(false)
  const [buscando, setBuscando] = useState(false)
  const [error, setError] = useState('')

  async function buscarHistorial(evento) {
    evento.preventDefault()

    const valor = busqueda.trim()

    if (!valor) {
      setError('Ingresa un DNI o nombre para buscar.')
      return
    }

    if (tipoBusqueda === 'dni' && !/^\d{8}$/.test(valor)) {
      setError('El DNI debe contener exactamente 8 dígitos.')
      return
    }

    setBuscando(true)
    setError('')
    setAtenciones([])
    setPacienteEncontrado(false)

    const ruta = tipoBusqueda === 'dni'
      ? `/api/atenciones/historial/dni/${encodeURIComponent(valor)}`
      : `/api/atenciones/historial/nombre?nombre=${encodeURIComponent(valor)}`

    try {
      const respuesta = await fetch(ruta)
      const datos = await respuesta.json().catch(() => null)

      if (!respuesta.ok) {
        setError(
          datos?.mensaje ||
          'No se pudo encontrar el paciente. Verifica los datos ingresados.'
        )
        return
      }

      setAtenciones(datos)
      setPacienteEncontrado(true)
    } catch {
      setError('No se pudo conectar con el servidor. Inténtalo de nuevo.')
    } finally {
      setBuscando(false)
    }
  }

  return (
    <main className="seccion-amplia">
      <div className="encabezado-seccion">
        <h1>Historial clínico del paciente</h1>
        {volver && (
          <button type="button" className="btn-secundario" onClick={volver} disabled={buscando}>
            + Nueva Atención
          </button>
        )}
      </div>

      <form onSubmit={buscarHistorial}>
        <label htmlFor="tipo-busqueda">
          Buscar paciente por
        </label>

        <select
          id="tipo-busqueda"
          value={tipoBusqueda}
          onChange={(evento) => {
            setTipoBusqueda(evento.target.value)
            setBusqueda('')
            setAtenciones([])
            setPacienteEncontrado(false)
            setError('')
          }}
          disabled={buscando}
        >
          <option value="dni">DNI</option>
          <option value="nombre">Nombre</option>
        </select>

        <label htmlFor="busqueda-paciente">
          {tipoBusqueda === 'dni' ? 'DNI del paciente' : 'Nombre del paciente'}
        </label>

        <input
          id="busqueda-paciente"
          type="text"
          value={busqueda}
          onChange={(evento) => setBusqueda(evento.target.value)}
          placeholder={
            tipoBusqueda === 'dni'
              ? 'Ejemplo: 12345678'
              : 'Ejemplo: Juan Pérez'
          }
          disabled={buscando}
          required
        />

        <button type="submit" disabled={buscando}>
          {buscando ? 'Buscando…' : 'Buscar historial'}
        </button>
      </form>

      {error && (
        <p className="error" role="alert">
          {error}
        </p>
      )}

      {pacienteEncontrado && (
        <section>
          <h2>Atenciones encontradas</h2>

          {atenciones.length === 0 ? (
            <p role="status">
              El paciente existe, pero no tiene atenciones registradas.
            </p>
          ) : (
            <>
              <p>
                Total de atenciones: {atenciones.length}
              </p>

              <div className="tabla-historial">
                <table>
                  <thead>
                    <tr>
                      <th>Fecha</th>
                      <th>Paciente</th>
                      <th>Diagnóstico</th>
                      <th>Tratamiento</th>
                    </tr>
                  </thead>

                  <tbody>
                    {atenciones.map((atencion) => (
                      <tr key={atencion.id}>
                        <td>{atencion.fecha}</td>
                        <td>{atencion.pacienteNombre}</td>
                        <td>{atencion.diagnostico}</td>
                        <td>{atencion.tratamiento}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </>
          )}
        </section>
      )}
    </main>
  )
}
