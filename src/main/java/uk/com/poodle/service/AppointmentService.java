package uk.com.poodle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Service;
import uk.com.poodle.client.PatientServiceClient;
import uk.com.poodle.data.AppointmentRepository;
import uk.com.poodle.domain.AddAppointmentNotesParams;
import uk.com.poodle.domain.AddAppointmentParams;
import uk.com.poodle.domain.Appointment;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static uk.com.poodle.service.AppointmentMapper.map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final DateTimeProvider dateTimeProvider;
    private final PatientServiceClient patientServiceClient;

    public Appointment addAppointment(AddAppointmentParams params) {
        var patient = patientServiceClient.getPatient(params.getPatientId()).orElseThrow(() -> new IllegalArgumentException("Patient not found."));
        log.info("Adding appointment for {}.", patient.getId());
        var entity = map(params);
        var savedEntity = repository.save(entity);
        log.info("Added appointment on {} for {}.", params.getDateTime(), patient.getId());
        return map(savedEntity);
    }

    public Appointment addAppointmentNotes(String appointmentId, AddAppointmentNotesParams params) {
        var appointmentEntity = repository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found."));
        log.info("Adding notes to appointment {}.", appointmentId);
        var savedEntity = repository.save(appointmentEntity.withNotes(params.getNotes()));
        log.info("Added notes to appointment {}.", appointmentId);
        return map(savedEntity);
    }

    public List<Appointment> getAppointments(String patientId, boolean includeHistoric) {
        log.info("Retrieving appointments for patient {}.", patientId);
        var appointments = includeHistoric
            ? repository.findAllByPatientId(patientId)
            : repository.findAllByPatientIdAndDateTimeGreaterThanEqual(patientId, now());
        return appointments.stream()
            .map(AppointmentMapper::map)
            .collect(toList());
    }

    private LocalDateTime now() {
        return dateTimeProvider.getNow()
            .map(LocalDateTime.class::cast)
            .orElseThrow();
    }
}
