import { useEffect, useState } from 'react'

async function cargarOpciones(ruta, signal) {
  const respuesta = await fetch(ruta, { signal })
  if (!respuesta.ok) {
    throw new Error('No se pudo cargar el catálogo.')
  }
  return respuesta.json()
}

export default function ProgramarCita({ volver }) {
  const [pacientes, setPacientes] = useState([])
  const [odontologos, setOdontologos] = useState([])
  const [cargando, setCargando] = useState(true)
  const [guardando, setGuardando] = useState(false)
  const [confirmacion, setConfirmacion] = useState('')
  const [error, setError] = useState('')
  const [fechaMinima] = useState(() => {
    const hoy = new Date()
    const mes = String(hoy.getMonth() + 1).padStart(2, '0')
    const dia = String(hoy.getDate()).padStart(2, '0')
    return `${hoy.getFullYear()}-${mes}-${dia}`
  })

  useEffect(() => {
    const controlador = new AbortController()

    async function cargarCatalogos() {
      try {
        const [listaPacientes, listaOdontologos] = await Promise.all([
          cargarOpciones('/api/pacientes', controlador.signal),
          cargarOpciones('/api/odontologos', controlador.signal),
        ])
        if (!controlador.signal.aborted) {
          setPacientes(listaPacientes)
          setOdontologos(listaOdontologos)
        }
      } catch {
        if (!controlador.signal.aborted) {
          setError('No se pudieron cargar los pacientes y odontólogos. Verifica que Spring Boot esté en ejecución y vuelve a abrir el formulario.')
        }
      } finally {
        if (!controlador.signal.aborted) {
          setCargando(false)
        }
      }
    }

    cargarCatalogos()
    return () => controlador.abort()
  }, [])

  async function guardarCita(evento) {
    evento.preventDefault()

    const formulario = evento.currentTarget
    const campos = new FormData(formulario)
    const cita = {
      pacienteId: Number(campos.get('pacienteId')),
      odontologoId: Number(campos.get('odontologoId')),
      fecha: campos.get('fecha'),
      hora: `${campos.get('hora')}:00`,
    }

    setGuardando(true)
    setConfirmacion('')
    setError('')

    try {
      const respuesta = await fetch('/api/citas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(cita),
      })
      const datos = await respuesta.json().catch(() => null)

      if (!respuesta.ok) {
        const detalles = Object.values(datos?.detalles ?? {}).join(' ')
        setError(detalles || datos?.mensaje || 'No se pudo programar la cita.')
        return
      }

      formulario.reset()
      setConfirmacion('Cita programada correctamente.')
    } catch {
      setError('No se pudo conectar con el servidor. Inténtalo de nuevo.')
    } finally {
      setGuardando(false)
    }
  }

  const hayOpciones = pacientes.length > 0 && odontologos.length > 0

  return (
    <main className="programar-cita">
      <div className="encabezado-seccion">
        <h1>Programar cita</h1>
        {volver && (
          <button type="button" className="btn-secundario" onClick={volver} disabled={guardando}>
            ← Ver Citas
          </button>
        )}
      </div>

      {cargando && <p role="status">Cargando pacientes y odontólogos…</p>}
      {!cargando && !error && !hayOpciones && (
        <p role="status">Debe haber al menos un paciente y un odontólogo registrados.</p>
      )}

      <form onSubmit={guardarCita}>
        <div className="campo-cita">
          <label htmlFor="cita-paciente">Paciente</label>
          <select id="cita-paciente" name="pacienteId" defaultValue="" disabled={cargando || guardando} required>
            <option value="" disabled>Selecciona un paciente</option>
            {pacientes.map((paciente) => (
              <option key={paciente.id} value={paciente.id}>
                {paciente.nombre} {paciente.apellidos} ({paciente.dni})
              </option>
            ))}
          </select>
        </div>

        <div className="campo-cita">
          <label htmlFor="cita-odontologo">Odontólogo</label>
          <select id="cita-odontologo" name="odontologoId" defaultValue="" disabled={cargando || guardando} required>
            <option value="" disabled>Selecciona un odontólogo</option>
            {odontologos.map((odontologo) => (
              <option key={odontologo.id} value={odontologo.id}>
                {odontologo.nombres} {odontologo.apellidos}
              </option>
            ))}
          </select>
        </div>

        <label>
          Fecha
          <input name="fecha" type="date" min={fechaMinima} disabled={guardando} required />
        </label>

        <label>
          Hora
          <input name="hora" type="time" disabled={guardando} required />
        </label>

        <button type="submit" disabled={cargando || guardando || !hayOpciones}>
          {guardando ? 'Guardando…' : 'Guardar cita'}
        </button>
      </form>

      {confirmacion && <p className="confirmacion" role="status">{confirmacion}</p>}
      {error && <p className="error" role="alert">{error}</p>}
    </main>
  )
}
