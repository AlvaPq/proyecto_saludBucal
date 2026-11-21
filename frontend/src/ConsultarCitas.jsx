import { useEffect, useState } from 'react'

function formatearHora(horaStr) {
  if (!horaStr) return ''
  return horaStr.slice(0, 5)
}

export default function ConsultarCitas({ irAProgramar, volver }) {
  const [citas, setCitas] = useState([])
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState('')
  const [mensajeAccion, setMensajeAccion] = useState('')
  const [filtroEstado, setFiltroEstado] = useState('TODAS')
  const [procesandoId, setProcesandoId] = useState(null)

  async function cargarCitas(signal) {
    try {
      const respuesta = await fetch('/api/citas', { signal })
      if (!respuesta.ok) throw new Error('Error al obtener citas')
      const datos = await respuesta.json()
      setCitas(datos)
    } catch (err) {
      if (!signal?.aborted) {
        setError('No se pudo cargar la lista de citas.')
      }
    } finally {
      if (!signal?.aborted) setCargando(false)
    }
  }

  useEffect(() => {
    const controlador = new AbortController()
    cargarCitas(controlador.signal)
    return () => controlador.abort()
  }, [])

  async function cancelarCita(id) {
    if (!window.confirm('¿Seguro que deseas cancelar esta cita?')) return

    setProcesandoId(id)
    setMensajeAccion('')
    setError('')

    try {
      const respuesta = await fetch(`/api/citas/${id}/cancelar`, {
        method: 'PATCH',
      })

      if (respuesta.ok) {
        const citaActualizada = await respuesta.json()
        setCitas((anteriores) =>
          anteriores.map((c) => (c.id === id ? citaActualizada : c))
        )
        setMensajeAccion(`Cita #${id} cancelada con éxito.`)
      } else {
        const err = await respuesta.json().catch(() => null)
        setError(err?.mensaje || 'No se pudo cancelar la cita.')
      }
    } catch {
      setError('Error al comunicarse con el servidor.')
    } finally {
      setProcesandoId(null)
    }
  }

  const citasFiltradas = citas.filter((c) => {
    if (filtroEstado === 'TODAS') return true
    return c.estado === filtroEstado
  })

  return (
    <main className="seccion-amplia">
      <div className="encabezado-seccion">
        <div>
          <h1>Control de Citas Odontológicas</h1>
          <p className="subtitulo">Visualiza, filtra y gestiona las citas programadas</p>
        </div>
        <div className="acciones-encabezado">
          {irAProgramar && (
            <button type="button" onClick={irAProgramar}>
              + Programar Cita
            </button>
          )}
          {volver && (
            <button type="button" className="btn-secundario" onClick={volver}>
              ← Volver
            </button>
          )}
        </div>
      </div>

      <div className="filtros-barra">
        <label>
          Filtrar por estado:
          <select value={filtroEstado} onChange={(e) => setFiltroEstado(e.target.value)}>
            <option value="TODAS">Todas ({citas.length})</option>
            <option value="PROGRAMADA">Programadas ({citas.filter(c => c.estado === 'PROGRAMADA').length})</option>
            <option value="ATENDIDA">Atendidas ({citas.filter(c => c.estado === 'ATENDIDA').length})</option>
            <option value="CANCELADA">Canceladas ({citas.filter(c => c.estado === 'CANCELADA').length})</option>
          </select>
        </label>
      </div>

      {mensajeAccion && <p className="confirmacion" role="status">{mensajeAccion}</p>}
      {error && <p className="error" role="alert">{error}</p>}
      {cargando && <p role="status">Cargando citas…</p>}

      {!cargando && citasFiltradas.length === 0 && (
        <p className="mensaje-vacio">No hay citas en este estado.</p>
      )}

      {!cargando && citasFiltradas.length > 0 && (
        <div className="tabla-contenedor">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Paciente</th>
                <th>Odontólogo</th>
                <th>Estado</th>
                <th>Acción</th>
              </tr>
            </thead>
            <tbody>
              {citasFiltradas.map((c) => (
                <tr key={c.id}>
                  <td>#{c.id}</td>
                  <td><strong>{c.fecha}</strong></td>
                  <td>{formatearHora(c.hora)}</td>
                  <td>{c.pacienteNombre}</td>
                  <td>{c.odontologoNombre}</td>
                  <td>
                    <span className={`badge badge-${c.estado.toLowerCase()}`}>
                      {c.estado}
                    </span>
                  </td>
                  <td>
                    {c.estado === 'PROGRAMADA' ? (
                      <button
                        type="button"
                        className="btn-peligro btn-sm"
                        disabled={procesandoId === c.id}
                        onClick={() => cancelarCita(c.id)}
                      >
                        {procesandoId === c.id ? 'Cancelando…' : 'Cancelar'}
                      </button>
                    ) : (
                      <span className="texto-muted">—</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </main>
  )
}
