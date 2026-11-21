import { useState } from 'react'

export default function RegistrarPaciente({ irAConsulta, volver }) {
  const [guardando, setGuardando] = useState(false)
  const [confirmacion, setConfirmacion] = useState('')
  const [error, setError] = useState('')

  async function registrar(evento) {
    evento.preventDefault()

    const formulario = evento.currentTarget
    const campos = new FormData(formulario)
    const paciente = {
      dni: campos.get('dni').trim(),
      nombres: campos.get('nombres').trim(),
      apellidos: campos.get('apellidos').trim(),
      edad: Number(campos.get('edad')),
    }

    setConfirmacion('')
    setError('')

    if (!paciente.nombres || !paciente.apellidos) {
      setError('Completa los nombres y apellidos del paciente.')
      return
    }

    setGuardando(true)

    try {
      const respuesta = await fetch('/api/pacientes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(paciente),
      })

      if (respuesta.status === 201) {
        formulario.reset()
        setConfirmacion(
          `Paciente ${paciente.nombres} ${paciente.apellidos} registrado correctamente.`,
        )
        return
      }

      const datos = await respuesta.json().catch(() => null)

      if (respuesta.status === 409) {
        setError(datos?.mensaje || 'Ya existe un paciente con ese DNI.')
      } else if (respuesta.status === 400) {
        const detalles = Object.values(datos?.detalles ?? {}).join(' ')
        setError(detalles || datos?.mensaje || 'Revisa los datos ingresados.')
      } else {
        setError('No se pudo guardar el paciente. Revisa que el backend esté en ejecución.')
      }
    } catch {
      setError('No se pudo conectar con el servidor. Inténtalo de nuevo.')
    } finally {
      setGuardando(false)
    }
  }

  return (
    <main>
      <div className="encabezado-seccion">
        <h1>Registrar paciente</h1>
        {irAConsulta && (
          <button type="button" className="btn-secundario" onClick={irAConsulta}>
            Ver Pacientes →
          </button>
        )}
      </div>

      <form onSubmit={registrar}>
        <label>
          DNI
          <input
            name="dni"
            inputMode="numeric"
            pattern="[0-9]{8}"
            maxLength={8}
            placeholder="8 dígitos numéricos"
            title="Ingresa los 8 dígitos del DNI"
            required
          />
        </label>

        <label>
          Nombres
          <input name="nombres" maxLength={100} placeholder="Ej. Juan Carlos" required />
        </label>

        <label>
          Apellidos
          <input name="apellidos" maxLength={100} placeholder="Ej. Pérez Quispe" required />
        </label>

        <label>
          Edad
          <input name="edad" type="number" min={0} max={130} step={1} placeholder="Ej. 25" required />
        </label>

        <button type="submit" disabled={guardando}>
          {guardando ? 'Guardando…' : 'Guardar paciente'}
        </button>
      </form>

      {confirmacion && <p className="confirmacion" role="status">{confirmacion}</p>}
      {error && <p className="error" role="alert">{error}</p>}
    </main>
  )
}
