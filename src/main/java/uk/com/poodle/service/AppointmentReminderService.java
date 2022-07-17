package uk.com.poodle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.com.poodle.client.PatientServiceClient;
import uk.com.poodle.client.domain.ContactDetails;
import uk.com.poodle.client.domain.Guardian;
import uk.com.poodle.client.domain.Patient;
import uk.com.poodle.data.AppointmentEntity;
import uk.com.poodle.data.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Collections.emptyList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentReminderService {

    static final String SUBJECT = "Reminder of your SLT appointment.";

    private final DateTimeProvider dateTimeProvider;
    private final AppointmentRepository appointmentRepository;
    private final MailService mailService;
    private final PatientServiceClient patientServiceClient;

    @Transactional
    @Scheduled(cron = "${service.scheduler.appointment-reminder.cron}")
    @SchedulerLock(
        name = "${service.scheduler.appointment-reminder.lock-name}",
        lockAtMostFor = "${service.scheduler.appointment-reminder.lock-for}")
    public void remind() {
        log.info("Running appointment reminder.");
        var appointments = appointmentRepository.findAllByDateTimeAfter(getStartOfTomorrow());
        appointments.forEach(this::sendReminder);
    }

    private LocalDateTime getStartOfTomorrow() {
        return dateTimeProvider.getNow()
            .map(LocalDateTime.class::cast)
            .map(LocalDateTime::toLocalDate)
            .map(localDate -> localDate.plusDays(1L))
            .map(LocalDate::atStartOfDay)
            .orElseThrow();
    }

    private void sendReminder(AppointmentEntity appointment) {
        var emailAddress = patientServiceClient.getPatient(appointment.getPatientId())
            .map(Patient::getGuardians).orElse(emptyList())
            .stream()
            .map(Guardian::getContactDetails)
            .map(ContactDetails::getEmail).toList();
        log.info("Sending reminder email(s) to {}.", String.join(",", emailAddress));
        mailService.send(emailAddress.get(0), SUBJECT, buildEmailContent(appointment));
    }

    private String buildEmailContent(AppointmentEntity appointment) {
        var appointmentTime = appointment.getDateTime();
        return "Your appointment is at " + appointmentTime.format(DateTimeFormatter.ofPattern("hh:mm")) + ".";
    }
}
