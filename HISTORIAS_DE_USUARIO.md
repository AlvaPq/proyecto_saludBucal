# 📋 Historias de Usuario — 1er Avance

## Proyecto: Sistema de Gestión Odontológica (Salud Bucal)
**Materia / Curso:** Proyecto Integrador  
**Equipo:** Bruno, Fabriscio  
**Fecha:** Noviembre 2026  

---

### Épica 1: Gestión de Pacientes

#### HU-01: Registro de Pacientes
- **Como** recepcionista / personal de salud,
- **Quiero** registrar a un paciente con su DNI, nombres, apellidos y edad,
- **Para** mantener una base de datos de los pacientes atendidos en la clínica.

**Criterios de Aceptación:**
- **CA-01 (Datos obligatorios):** El DNI (8 dígitos numéricos), nombres, apellidos y edad (>= 0 y <= 130) son obligatorios.
- **CA-02 (DNI único):** No se permite duplicar DNIs. Si ya existe, el sistema responde con error HTTP 409 (Conflicto) y mensaje descriptivo.
- **CA-03 (Confirmación):** Al crearse correctamente, el sistema responde con HTTP 201 y muestra confirmación visual en pantalla.

---

#### HU-02: Búsqueda y Consulta de Pacientes
- **Como** odontólogo o recepcionista,
- **Quiero** buscar pacientes por DNI o por coincidencia en sus nombres y apellidos,
- **Para** verificar su información de forma rápida e intuitiva.

**Criterios de Aceptación:**
- **CA-01 (Búsqueda en tiempo real):** El listado filtra instantáneamente mientras el usuario escribe en el buscador.
- **CA-02 (Accent Insensitive):** La búsqueda ignora tildes y mayúsculas/minúsculas (ej. "Pérez" coincide con "perez").
- **CA-03 (Estado vacío):** Si no hay coincidencias, el sistema informa amigablemente al usuario.

---

### Épica 2: Gestión de Odontólogos del Staff

#### HU-03: Registro y Consulta de Odontólogos *(Nueva en 1er Avance)*
- **Como** administrador de la clínica,
- **Quiero** registrar a los odontólogos del consultorio con su DNI y especialidad/nombres,
- **Para** disponer de la lista de profesionales asignables a las citas médicas.

**Criterios de Aceptación:**
- **CA-01 (Validación de DNI):** DNI de exactamente 8 dígitos numéricos y único.
- **CA-02 (Disponibilidad inmediata):** El odontólogo registrado queda disponible inmediatamente en el formulario de citas.
- **CA-03 (Listado en tiempo real):** Permite ver y buscar entre los odontólogos activos del consultorio.

---

### Épica 3: Programación y Control de Citas

#### HU-04: Programación de Cita Odontológica
- **Como** recepcionista o paciente,
- **Quiero** agendar una cita seleccionando el paciente, el odontólogo, la fecha y la hora,
- **Para** asegurar la atención sin cruces de horarios.

**Criterios de Aceptación:**
- **CA-01 (Validación de fecha futura):** Solo se permiten citas para la fecha actual o futuras.
- **CA-02 (Control de conflictos del odontólogo):** Un odontólogo no puede tener dos citas en el mismo día y hora (responde con error 409 `HorarioOcupadoException`).
- **CA-03 (Control de conflictos del paciente):** Un paciente no puede tener dos citas agendadas al mismo tiempo.
- **CA-04 (Estado inicial):** La cita se genera en estado `PROGRAMADA`.

---

#### HU-05: Visualización y Cancelación de Citas *(Nueva en 1er Avance)*
- **Como** recepcionista u odontólogo,
- **Quiero** consultar la agenda de citas programadas y tener la opción de cancelar citas,
- **Para** tener el control operativo del consultorio día a día.

**Criterios de Aceptación:**
- **CA-01 (Filtro por estado):** Permite filtrar citas por estado (`TODAS`, `PROGRAMADA`, `ATENDIDA`, `CANCELADA`).
- **CA-02 (Cancelación segura):** Solo las citas en estado `PROGRAMADA` muestran el botón para cancelar. Requiere confirmación del usuario.
- **CA-03 (Persistencia de estado):** Al cancelar, el endpoint `PATCH /api/citas/{id}/cancelar` actualiza la cita y el badge visual cambia a `CANCELADA`.

---

### Épica 4: Atención Clínica e Historial

#### HU-06: Registro de Atención Odontológica
- **Como** odontólogo,
- **Quiero** registrar los detalles clínicos (diagnóstico y tratamiento) de una atención brindada a un paciente,
- **Para** alimentar el historial médico y expediente del paciente.

**Criterios de Aceptación:**
- **CA-01 (Selección de paciente):** Se selecciona el paciente de un catálogo interactivo con DNI.
- **CA-02 (Campos clínicos):** Diagnóstico y tratamiento son textos enriquecidos de hasta 2000 caracteres.
- **CA-03 (Confirmación inmediata):** Al registrar con éxito, se limpia el formulario y se notifica al usuario.

---

#### HU-07: Consulta de Historial Clínico
- **Como** odontólogo,
- **Quiero** buscar el historial clínico completo de un paciente ingresando su DNI o su nombre,
- **Para** revisar antecedentes, diagnósticos previos y tratamientos aplicados.

**Criterios de Aceptación:**
- **CA-01 (Búsqueda dual):** Permite alternar entre búsqueda exacta por DNI (8 dígitos) o búsqueda por nombre/apellido.
- **CA-02 (Cronología):** Las atenciones se presentan ordenadas por fecha más reciente primero.
- **CA-03 (Mensaje descriptivo):** Si el paciente existe pero no tiene atenciones, se especifica claramente la situación.
