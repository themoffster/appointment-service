package uk.com.poodle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.auditing.DateTimeProvider;
import uk.com.poodle.client.PatientServiceClient;
import uk.com.poodle.client.domain.ContactDetails;
import uk.com.poodle.client.domain.Guardian;
import uk.com.poodle.client.domain.Patient;
import uk.com.poodle.data.AppointmentEntity;
import uk.com.poodle.data.AppointmentRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static uk.com.poodle.Constants.APPOINTMENT_DATE_TIME;
import static uk.com.poodle.Constants.PATIENT_ID;
import static uk.com.poodle.service.AppointmentReminderService.SUBJECT;

@ExtendWith(MockitoExtension.class)
class AppointmentReminderServiceTest {

    @Mock
    private DateTimeProvider mockDateTimeProvider;

    @Mock
    private AppointmentRepository mockAppointmentRepository;

    @Mock
    private PatientServiceClient mockPatientServiceClient;

    @Mock
    private MailService mockMailService;

    @InjectMocks
    private AppointmentReminderService appointmentReminderService;

    @Test
    void shouldSendReminderForTomorrowsAppointments() {
        var now = LocalDateTime.of(now(), LocalTime.MIN);
        var appointment = AppointmentEntity.builder()
            .dateTime(APPOINTMENT_DATE_TIME)
            .patientId(PATIENT_ID)
            .build();
        var guardian = Guardian.builder()
            .contactDetails(ContactDetails.builder()
                .email("foo@bar.com")
                .build())
            .build();
        var patient = Patient.builder()
            .id(PATIENT_ID)
            .guardians(List.of(guardian))
            .build();
        when(mockDateTimeProvider.getNow()).thenReturn(Optional.of(now));
        when(mockPatientServiceClient.getPatient(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(mockAppointmentRepository.findAllByDateTimeAfter(now.plusDays(1L))).thenReturn(List.of(appointment));
        doNothing().when(mockMailService).send("foo@bar.com", SUBJECT, "Your appointment is at 09:00.");

        appointmentReminderService.remind();
    }
}
