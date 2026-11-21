package com.proyecto.saludBucal.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.proyecto.saludBucal.entity.Atencion;
import com.proyecto.saludBucal.entity.Cita;
import com.proyecto.saludBucal.entity.EstadoCita;
import com.proyecto.saludBucal.entity.Odontologo;
import com.proyecto.saludBucal.entity.Paciente;
import com.proyecto.saludBucal.repository.AtencionRepository;
import com.proyecto.saludBucal.repository.CitaRepository;
import com.proyecto.saludBucal.repository.OdontologoRepository;
import com.proyecto.saludBucal.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final CitaRepository citaRepository;
    private final AtencionRepository atencionRepository;

    @Override
    public void run(String... args) {
        // 1. Inicializar Odontólogos
        crearOdontologoSiNoExiste("10293847", "Carlos Alberto", "Mendoza Vargas");
        crearOdontologoSiNoExiste("48596071", "Ana Patricia", "Torres Linares");
        crearOdontologoSiNoExiste("32819045", "Roberto Luis", "Vega Morales");
        crearOdontologoSiNoExiste("51209384", "Elena Sofía", "Paredes Quiroz");

        // 2. Inicializar Pacientes
        crearPacienteSiNoExiste("72819345", "Luciana", "Ramos Paredes", 28);
        crearPacienteSiNoExiste("09876543", "Miguel Angel", "Castro Soto", 42);
        crearPacienteSiNoExiste("45678901", "Valeria Sofía", "Benítez Ríos", 22);
        crearPacienteSiNoExiste("60192837", "Jorge Luis", "Huamán Chávez", 35);
        crearPacienteSiNoExiste("81920394", "Carmen Rosa", "Quispe Mamani", 54);

        List<Paciente> pacientes = pacienteRepository.findAll();
        List<Odontologo> odontologos = odontologoRepository.findAll();

        if (pacientes.size() >= 4 && odontologos.size() >= 3) {
            // 3. Inicializar Citas si hay menos de 3
            if (citaRepository.count() < 3) {
                log.info("Cargando citas de demostración...");
                
                citaRepository.save(Cita.builder()
                        .fecha(LocalDate.now().plusDays(1))
                        .hora(LocalTime.of(9, 0))
                        .estado(EstadoCita.PROGRAMADA)
                        .paciente(pacientes.get(0))
                        .odontologo(odontologos.get(0))
                        .build());

                citaRepository.save(Cita.builder()
                        .fecha(LocalDate.now().plusDays(2))
                        .hora(LocalTime.of(11, 30))
                        .estado(EstadoCita.PROGRAMADA)
                        .paciente(pacientes.get(1))
                        .odontologo(odontologos.get(1))
                        .build());

                citaRepository.save(Cita.builder()
                        .fecha(LocalDate.now().plusDays(3))
                        .hora(LocalTime.of(15, 0))
                        .estado(EstadoCita.PROGRAMADA)
                        .paciente(pacientes.get(2))
                        .odontologo(odontologos.get(2))
                        .build());

                citaRepository.save(Cita.builder()
                        .fecha(LocalDate.now().minusDays(2))
                        .hora(LocalTime.of(10, 0))
                        .estado(EstadoCita.ATENDIDA)
                        .paciente(pacientes.get(3))
                        .odontologo(odontologos.get(0))
                        .build());

                citaRepository.save(Cita.builder()
                        .fecha(LocalDate.now().minusDays(5))
                        .hora(LocalTime.of(16, 30))
                        .estado(EstadoCita.CANCELADA)
                        .paciente(pacientes.get(0))
                        .odontologo(odontologos.get(1))
                        .build());
            }

            // 4. Inicializar Atenciones si hay menos de 3
            if (atencionRepository.count() < 3) {
                log.info("Cargando atenciones clínicas de historial...");

                atencionRepository.save(Atencion.builder()
                        .fecha(LocalDate.now().minusDays(15))
                        .diagnostico("Caries dental profunda en pieza 4.6 y gingivitis leve marginal.")
                        .tratamiento("Profilaxis dental ultrasonica y curación con resina fotocurable.")
                        .paciente(pacientes.get(0))
                        .build());

                atencionRepository.save(Atencion.builder()
                        .fecha(LocalDate.now().minusDays(6))
                        .diagnostico("Pulpitis irreversible sintomática en molar 3.6.")
                        .tratamiento("Tratamiento biomecánico de conductos (endodoncia) y sellado provisional.")
                        .paciente(pacientes.get(1))
                        .build());

                atencionRepository.save(Atencion.builder()
                        .fecha(LocalDate.now().minusDays(20))
                        .diagnostico("Maloclusión clase I con apiñamiento anteroinferior moderado.")
                        .tratamiento("Toma de impresiones diagnósticas, análisis cefalométrico y colocación de brackets.")
                        .paciente(pacientes.get(2))
                        .build());

                atencionRepository.save(Atencion.builder()
                        .fecha(LocalDate.now().minusDays(30))
                        .diagnostico("Hipersensibilidad dentinaria generalizada y cálculo supragingival.")
                        .tratamiento("Destartraje ultrasónico completo, pulido coronario y aplicación de barniz de flúor.")
                        .paciente(pacientes.get(3))
                        .build());
            }
        }
    }

    private void crearOdontologoSiNoExiste(String dni, String nombres, String apellidos) {
        if (!odontologoRepository.existsByDni(dni)) {
            odontologoRepository.save(Odontologo.builder()
                    .dni(dni)
                    .nombres(nombres)
                    .apellidos(apellidos)
                    .build());
        }
    }

    private void crearPacienteSiNoExiste(String dni, String nombres, String apellidos, Integer edad) {
        if (!pacienteRepository.existsByDni(dni)) {
            pacienteRepository.save(Paciente.builder()
                    .dni(dni)
                    .nombre(nombres)
                    .apellidos(apellidos)
                    .edad(edad)
                    .build());
        }
    }
}
