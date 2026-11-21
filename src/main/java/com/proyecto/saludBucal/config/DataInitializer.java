package com.proyecto.saludBucal.config;

import java.time.LocalDate;
import java.time.LocalTime;

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
        // Inicializar Odontólogos si no existen
        if (odontologoRepository.count() == 0) {
            log.info("Inicializando catálogo de odontólogos...");
            odontologoRepository.save(Odontologo.builder()
                    .dni("10293847")
                    .nombres("Carlos Alberto")
                    .apellidos("Mendoza Vargas")
                    .build());

            odontologoRepository.save(Odontologo.builder()
                    .dni("48596071")
                    .nombres("Ana Patricia")
                    .apellidos("Torres Linares")
                    .build());
        }

        // Inicializar Pacientes si no existen
        if (pacienteRepository.count() == 0) {
            log.info("Inicializando pacientes de prueba...");
            pacienteRepository.save(Paciente.builder()
                    .dni("72819345")
                    .nombre("Luciana")
                    .apellidos("Ramos Paredes")
                    .edad(28)
                    .build());

            pacienteRepository.save(Paciente.builder()
                    .dni("09876543")
                    .nombre("Miguel Angel")
                    .apellidos("Castro Soto")
                    .edad(42)
                    .build());
        }

        // Inicializar 1 Cita si no existen
        if (citaRepository.count() == 0 && pacienteRepository.count() > 0 && odontologoRepository.count() > 0) {
            log.info("Inicializando cita de demostración...");
            Paciente paciente = pacienteRepository.findAll().get(0);
            Odontologo odontologo = odontologoRepository.findAll().get(0);

            citaRepository.save(Cita.builder()
                    .fecha(LocalDate.now().plusDays(2))
                    .hora(LocalTime.of(10, 30))
                    .estado(EstadoCita.PROGRAMADA)
                    .paciente(paciente)
                    .odontologo(odontologo)
                    .build());
        }

        // Inicializar 1 Atención de historial si no existen
        if (atencionRepository.count() == 0 && pacienteRepository.count() > 0) {
            log.info("Inicializando atención previa de demostración...");
            Paciente paciente = pacienteRepository.findAll().get(0);

            atencionRepository.save(Atencion.builder()
                    .fecha(LocalDate.now().minusDays(15))
                    .diagnostico("Caries dental profunda en pieza 4.6 y gingivitis leve.")
                    .tratamiento("Profilaxis dental y curación con resina fotocurable.")
                    .paciente(paciente)
                    .build());
        }
    }
}
