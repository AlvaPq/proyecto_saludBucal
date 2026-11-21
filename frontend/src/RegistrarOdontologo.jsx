import { useEffect, useState } from 'react'

function normalizar(texto) {
  return texto
    .trim()
    .toLowerCase()
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
}

export default function RegistrarOdontologo({ volver }) {
  const [odontologos, setOdontologos] = useState([])
  const [cargando, setCargando] = useState(true)
  const [guardando, setGuardando] = useState(false)
  const [confirmacion, setConfirmacion] = useState('')
  const [error, setError] = useState('')
  const [busqueda, setBusqueda] = useState('')

  async function cargarOdontologos(signal) {
    try {
      const respuesta = await fetch('/api/odontologos', { signal })
      if (!respuesta.ok) throw new Error('Error al cargar odontólogos')
      const datos = await respuesta.json()
      setOdontologos(datos)
    } catch (err) {
      if (!signal?.aborted) {
        setError('No se pudo cargar la lista de odontólogos.')
      }
    } finally {
      if (!signal?.aborted) setCargando(false)
    }
  }

  useEffect(() => {
    const controlador = new AbortController()
    cargarOdontologos(controlador.signal)
    return () => controlador.abort()
  }, [])

  async function registrar(evento) {
    evento.preventDefault()
    const formulario = evento.currentTarget
    const campos = new FormData(formulario)
    const nuevo = {
      dni: campos.get('dni').trim(),
      nombres: campos.get('nombres').trim(),
      apellidos: campos.get('apellidos').trim(),
    }

    setConfirmacion('')
    setError('')
    setGuardando(true)

    try {
      const respuesta = await fetch('/api/odontologos', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nuevo),
      })

      if (respuesta.status === 201) {
        formulario.reset()
        setConfirmacion(`Odontólogo(a) ${nuevo.nombres} ${nuevo.apellidos} registrado(a) correctamente.`)
        await cargarOdontologos()
        return
      }

      const datos = await respuesta.json().catch(() => null)
      if (respuesta.status === 409) {
        setError(datos?.mensaje || 'Ya existe un odontólogo con ese DNI.')
      } else if (respuesta.status === 400) {
        const detalles = Object.values(datos?.detalles ?? {}).join(' ')
        setError(detalles || datos?.mensaje || 'Datos no válidos.')
      } else {
        setError('No se pudo registrar el odontólogo.')
      }
    } catch {
      setError('Error de conexión con el servidor.')
    } finally {
      setGuardando(false)
    }
  }

  const criterio = normalizar(busqueda)
  const filtrados = odontologos.filter((o) => {
    const completo = normalizar(`${o.nombres} ${o.apellidos}`)
    return o.dni.includes(criterio) || completo.includes(criterio)
  })

  return (
    <main className="seccion-amplia">
      <div className="encabezado-seccion">
        <h1>Gestión de Odontólogos</h1>
        {volver && (
          <button type="button" className="btn-secundario" onClick={volver}>
            ← Volver
          </button>
        )}
      </div>

      <div className="grid-dos-columnas">
        <section className="tarjeta-formulario">
          <h2>Registrar Odontólogo</h2>
          <form onSubmit={registrar}>
            <label>
              DNI
              <input
                name="dni"
                inputMode="numeric"
                pattern="[0-9]{8}"
                maxLength={8}
                placeholder="8 dígitos"
                required
              />
            </label>

            <label>
              Nombres
              <input name="nombres" maxLength={100} placeholder="Ej. Carlos Alberto" required />
            </label>

            <label>
              Apellidos
              <input name="apellidos" maxLength={100} placeholder="Ej. Mendoza Vargas" required />
            </label>

            <button type="submit" disabled={guardando}>
              {guardando ? 'Guardando…' : 'Guardar Odontólogo'}
            </button>

            {confirmacion && <p className="confirmacion" role="status">{confirmacion}</p>}
            {error && <p className="error" role="alert">{error}</p>}
          </form>
        </section>

        <section className="tarjeta-lista">
          <h2>Odontólogos del Staff ({odontologos.length})</h2>
          <input
            type="search"
            placeholder="Buscar por DNI o apellido…"
            value={busqueda}
            onChange={(e) => setBusqueda(e.target.value)}
          />

          {cargando && <p>Cargando odontólogos…</p>}

          {!cargando && filtrados.length === 0 && (
            <p className="mensaje-vacio">No hay odontólogos que coincidan con la búsqueda.</p>
          )}

          {!cargando && filtrados.length > 0 && (
            <div className="tabla-contenedor">
              <table>
                <thead>
                  <tr>
                    <th>DNI</th>
                    <th>Nombres y Apellidos</th>
                  </tr>
                </thead>
                <tbody>
                  {filtrados.map((o) => (
                    <tr key={o.id}>
                      <td><strong>{o.dni}</strong></td>
                      <td>{o.nombres} {o.apellidos}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </main>
  )
}
