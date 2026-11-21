
import { useEffect, useState } from 'react'

export default function RegistrarAtencion({ volver }) {
  const [pacientes, setPacientes] = useState([])
  const [cargando, setCargando] = useState(true)
  const [guardando, setGuardando] = useState(false)
  const [confirmacion, setConfirmacion] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    const controlador = new AbortController()

    async function cargarPacientes() {
      try {
        const respuesta = await fetch('/api/pacientes', {
          signal: controlador.signal,
        })

        if (!respuesta.ok) {
          throw new Error('No se pudieron cargar los pacientes.')
        }

        const datos = await respuesta.json()

        if (!controlador.signal.aborted) {
          setPacientes(datos)
        }
      } catch {
        if (!controlador.signal.aborted) {
          setError('No se pudieron cargar los pacientes. Verifica que Spring Boot esté en ejecución.')
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

  async function guardarAtencion(evento) {
    evento.preventDefault()

    const formulario = evento.currentTarget
    const campos = new FormData(formulario)

    const atencion = {
      pacienteId: Number(campos.get('pacienteId')),
      fecha: campos.get('fecha'),
      diagnostico: campos.get('diagnostico').trim(),
      tratamiento: campos.get('tratamiento').trim(),
    }

    setGuardando(true)
    setConfirmacion('')
    setError('')

    try {
      const respuesta = await fetch('/api/atenciones', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(atencion),
      })

      const datos = await respuesta.json().catch(() => null)

      if (!respuesta.ok) {
        const detalles = Object.values(datos?.detalles ?? {}).join(' ')
        setError(detalles || datos?.mensaje || 'No se pudo registrar la atención.')
        return
      }

      formulario.reset()
      setConfirmacion('Atención registrada correctamente.')
    } catch {
      setError('No se pudo conectar con el servidor. Inténtalo de nuevo.')
    } finally {
      setGuardando(false)
    }
  }

  return (
    <main className="registrar-atencion">
      <h1>Registrar atención</h1>

      <button type="button" onClick={volver} disabled={guardando}>
        Volver al menú
      </button>

      {cargando && (
        <p role="status">Cargando pacientes…</p>
      )}

      {!cargando && pacientes.length === 0 && !error && (
        <p role="status">
          No hay pacientes registrados. Primero debes registrar un paciente.
        </p>
      )}

      <form onSubmit={guardarAtencion}>
        <div className="campo-cita">
          <label htmlFor="atencion-paciente">Paciente</label>

          <select
            id="atencion-paciente"
            name="pacienteId"
            defaultValue=""
            disabled={cargando || guardando || pacientes.length === 0}
            required>
            <option value="" disabled>
              Selecciona un paciente
            </option>

            {pacientes.map((paciente) => (
              <option key={paciente.id} value={paciente.id}>
                {paciente.nombre} {paciente.apellidos} ({paciente.dni})
              </option>
            ))}
          </select>
        </div>

        <label>
          Fecha de atención
          <input
            name="fecha"
            type="date"
            disabled={guardando}
            required
          />
        </label>

        <label>
          Diagnóstico
          <textarea
            name="diagnostico"
            maxLength={2000}
            rows={4}
            placeholder="Describe el diagnóstico del paciente"
            disabled={guardando}
            required
          />
        </label>

        <label>
          Tratamiento
          <textarea
            name="tratamiento"
            maxLength={2000}
            rows={4}
            placeholder="Describe el tratamiento realizado"
            disabled={guardando}
            required
          />
        </label>

        <button
          type="submit"
          disabled={cargando || guardando || pacientes.length === 0}>
          {guardando ? 'Guardando…' : 'Registrar atención'}
        </button>
      </form>

      {confirmacion && (
        <p className="confirmacion" role="status">
          {confirmacion}
        </p>
      )}

      {error && (
        <p className="error" role="alert">
          {error}
        </p>
      )}
    </main>
  )
}
