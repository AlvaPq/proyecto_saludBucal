import { useState } from 'react'
import RegistrarPaciente from './RegistrarPaciente.jsx'
import ConsultarPacientes from './ConsultarPacientes.jsx'
import RegistrarOdontologo from './RegistrarOdontologo.jsx'
import ProgramarCita from './ProgramarCita.jsx'
import ConsultarCitas from './ConsultarCitas.jsx'
import RegistrarAtencion from './RegistrarAtencion.jsx'
import HistorialAtenciones from './HistorialAtenciones.jsx'

export default function App() {
  const [pantalla, setPantalla] = useState('citas')

  return (
    <div className="app-layout">
      <header className="app-header">
        <div className="app-header-contenido">
          <div className="app-branding">
            <span style={{ fontSize: '1.7rem' }}>🦷</span>
            <div>
              <h1>Salud Bucal</h1>
            </div>
            <span className="app-badge-avance">1er Avance</span>
          </div>

          <nav className="app-nav" aria-label="Navegación principal">
            <button
              type="button"
              className={`nav-btn ${pantalla === 'citas' || pantalla === 'programar-cita' ? 'activo' : ''}`}
              onClick={() => setPantalla('citas')}
            >
              📅 Citas
            </button>
            <button
              type="button"
              className={`nav-btn ${pantalla === 'registro-paciente' || pantalla === 'consulta-pacientes' ? 'activo' : ''}`}
              onClick={() => setPantalla('consulta-pacientes')}
            >
              👤 Pacientes
            </button>
            <button
              type="button"
              className={`nav-btn ${pantalla === 'odontologos' ? 'activo' : ''}`}
              onClick={() => setPantalla('odontologos')}
            >
              👨‍⚕️ Odontólogos
            </button>
            <button
              type="button"
              className={`nav-btn ${pantalla === 'atencion' || pantalla === 'historial' ? 'activo' : ''}`}
              onClick={() => setPantalla('historial')}
            >
              📋 Atenciones
            </button>
          </nav>
        </div>
      </header>

      {/* Sub-barra de acciones contextuales según el módulo activo */}
      <div style={{ background: '#e4eee9', borderBottom: '1px solid #cfe0dc', padding: '0.4rem 2rem' }}>
        <div style={{ maxWidth: 1100, margin: '0 auto', display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
          {(pantalla === 'citas' || pantalla === 'programar-cita') && (
            <>
              <button
                type="button"
                className={`btn-sm ${pantalla === 'citas' ? 'btn-secundario' : ''}`}
                style={pantalla === 'citas' ? { fontWeight: 700 } : { background: 'transparent', color: '#173333' }}
                onClick={() => setPantalla('citas')}
              >
                Ver Citas Programadas
              </button>
              <button
                type="button"
                className={`btn-sm ${pantalla === 'programar-cita' ? 'btn-secundario' : ''}`}
                style={pantalla === 'programar-cita' ? { fontWeight: 700 } : { background: 'transparent', color: '#173333' }}
                onClick={() => setPantalla('programar-cita')}
              >
                + Programar Cita
              </button>
            </>
          )}

          {(pantalla === 'registro-paciente' || pantalla === 'consulta-pacientes') && (
            <>
              <button
                type="button"
                className={`btn-sm ${pantalla === 'consulta-pacientes' ? 'btn-secundario' : ''}`}
                style={pantalla === 'consulta-pacientes' ? { fontWeight: 700 } : { background: 'transparent', color: '#173333' }}
                onClick={() => setPantalla('consulta-pacientes')}
              >
                Consultar Pacientes
              </button>
              <button
                type="button"
                className={`btn-sm ${pantalla === 'registro-paciente' ? 'btn-secundario' : ''}`}
                style={pantalla === 'registro-paciente' ? { fontWeight: 700 } : { background: 'transparent', color: '#173333' }}
                onClick={() => setPantalla('registro-paciente')}
              >
                + Registrar Paciente
              </button>
            </>
          )}

          {(pantalla === 'atencion' || pantalla === 'historial') && (
            <>
              <button
                type="button"
                className={`btn-sm ${pantalla === 'historial' ? 'btn-secundario' : ''}`}
                style={pantalla === 'historial' ? { fontWeight: 700 } : { background: 'transparent', color: '#173333' }}
                onClick={() => setPantalla('historial')}
              >
                Consultar Historial Clínico
              </button>
              <button
                type="button"
                className={`btn-sm ${pantalla === 'atencion' ? 'btn-secundario' : ''}`}
                style={pantalla === 'atencion' ? { fontWeight: 700 } : { background: 'transparent', color: '#173333' }}
                onClick={() => setPantalla('atencion')}
              >
                + Registrar Nueva Atención
              </button>
            </>
          )}
        </div>
      </div>

      <div className="app-cuerpo">
        {pantalla === 'registro-paciente' && (
          <RegistrarPaciente irAConsulta={() => setPantalla('consulta-pacientes')} />
        )}

        {pantalla === 'consulta-pacientes' && (
          <ConsultarPacientes volver={() => setPantalla('registro-paciente')} />
        )}

        {pantalla === 'odontologos' && (
          <RegistrarOdontologo />
        )}

        {pantalla === 'citas' && (
          <ConsultarCitas irAProgramar={() => setPantalla('programar-cita')} />
        )}

        {pantalla === 'programar-cita' && (
          <ProgramarCita volver={() => setPantalla('citas')} />
        )}

        {pantalla === 'atencion' && (
          <RegistrarAtencion volver={() => setPantalla('historial')} />
        )}

        {pantalla === 'historial' && (
          <HistorialAtenciones volver={() => setPantalla('atencion')} />
        )}
      </div>
    </div>
  )
}
