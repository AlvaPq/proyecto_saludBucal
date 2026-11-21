import { useEffect, useState } from 'react'

function normalizar(texto) {
  return texto
    .trim()
    .toLowerCase()
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
}

export default function ConsultarPacientes({ volver }) {
  const [pacientes, setPacientes] = useState([])
  const [busqueda, setBusqueda] = useState('')
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const controlador = new AbortController()

    async function cargarPacientes() {
      try {
        const respuesta = await fetch('/api/pacientes', {
          signal: controlador.signal,
        })

        if (!respuesta.ok) {
          throw new Error('No se pudo cargar la lista de pacientes.')
        }

        const datos = await respuesta.json()
        setPacientes(datos)
      } catch {
        if (!controlador.signal.aborted) {
          setError('No se pudo cargar la lista. Revisa que Spring Boot esté en ejecución.')
        }
      } finally {
        if (!controlador.signal.aborted) {
          setCargando(false)
        }
      }
    }

    cargarPacientes()
    return () => controlador.abort()
  }, [])

  const criterio = normalizar(busqueda)
  const resultados = pacientes.filter((paciente) => {
    const nombreCompleto = normalizar(`${paciente.nombre} ${paciente.apellidos}`)
    return paciente.dni.includes(criterio) || nombreCompleto.includes(criterio)
  })

  return (
    <main className="consulta-pacientes">
      <div className="encabezado-seccion">
        <h1>Consultar pacientes</h1>
        {volver && (
          <button type="button" className="btn-secundario" onClick={volver}>
            + Nuevo Paciente
          </button>
        )}
      </div>

      <label className="buscador-pacientes">
        Buscar por DNI, nombre o apellido
        <input
          type="search"
          value={busqueda}
          onChange={(evento) => setBusqueda(evento.target.value)}
        />
      </label>

      {cargando && <p role="status">Cargando pacientes…</p>}
      {error && <p className="error" role="alert">{error}</p>}

      {!cargando && !error && (
        resultados.length === 0 ? (
          <p role="status">
            {pacientes.length === 0
              ? 'Todavía no hay pacientes registrados.'
              : 'No se encontraron pacientes con esa búsqueda.'}
          </p>
        ) : (
          <div className="tabla-pacientes">
            <table aria-label="Pacientes registrados">
              <thead>
                <tr>
                  <th scope="col">DNI</th>
                  <th scope="col">Nombres</th>
                  <th scope="col">Apellidos</th>
                  <th scope="col">Edad</th>
                </tr>
              </thead>
              <tbody>
                {resultados.map((paciente) => (
                  <tr key={paciente.id}>
                    <td>{paciente.dni}</td>
                    <td>{paciente.nombre}</td>
                    <td>{paciente.apellidos}</td>
                    <td>{paciente.edad}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )
      )}
    </main>
  )
}