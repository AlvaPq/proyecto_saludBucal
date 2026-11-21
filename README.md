# 🦷 Salud Bucal — Sistema de Gestión Odontológica
> **Proyecto Integrador — 1er Avance**  
> **Rama de desarrollo:** `Fabriscio`

Sistema web integral para la gestión de pacientes, odontólogos, programación de citas y registro de historias clínicas en un consultorio odontológico.

---

## 🚀 Tecnologías Utilizadas

- **Backend:** Java 17, Spring Boot 4.1.2, Spring Data JPA, Hibernate, MySQL Connector, Lombok, Jakarta Validation.
- **Frontend:** React 19, Vite 8, CSS3 Moderno, JavaScript ES Modules.
- **Base de Datos:** MySQL 8.0.

---

## 🛠️ Requisitos Previos

- **Java JDK 17** o superior instalado y configurado en el PATH.
- **Node.js 18+** y **npm**.
- **Servicio MySQL** en ejecución en el puerto 3306 (con usuario `root` y contraseña `mysql`, o configurada en `application.properties`).

---

## ⚡ Instrucciones de Ejecución

### 1. Iniciar el Backend (Spring Boot)
Desde la raíz del repositorio:
```powershell
.\mvnw.cmd spring-boot:run
```
> La API quedará disponible en: `http://localhost:8080`  
> *Nota:* Al iniciar por primera vez, el componente `DataInitializer` creará automáticamente datos de prueba (odontólogos, pacientes y citas) para facilitar la demostración.

### 2. Iniciar el Frontend (React + Vite)
En otra terminal, accede a la carpeta `frontend`:
```powershell
cd frontend
npm install
npm run dev
```
> La aplicación web abrirá en: `http://localhost:5173`

---

## 📦 Módulos del 1er Avance

1. **Gestión de Citas:**
   - Visualización de la agenda de citas con badges de estado (`PROGRAMADA`, `ATENDIDA`, `CANCELADA`).
   - Programación de nuevas citas con validación anti-solapamiento de horarios.
   - Cancelación segura de citas programadas.
2. **Gestión de Pacientes:**
   - Registro de pacientes con validación de DNI y rango de edad.
   - Búsqueda en tiempo real (ignora mayúsculas y acentos).
3. **Gestión de Odontólogos:**
   - Registro de odontólogos del staff.
   - Catálogo de profesionales activos disponibles para citas.
4. **Atención e Historial Clínico:**
   - Registro de diagnóstico y tratamiento odontológico.
   - Búsqueda de historial médico por DNI o por nombre.

Para ver el desglose completo de requisitos funcionales, consulta [HISTORIAS_DE_USUARIO.md](./HISTORIAS_DE_USUARIO.md).
