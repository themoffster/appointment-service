package uk.com.poodle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.auditing.DateTimeProvider;
import uk.com.poodle.client.PatientServiceClient;
import uk.com.poodle.client.domain.Patient;
import uk.com.poodle.data.AppointmentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static uk.com.poodle.Constants.APPOINTMENT_DATE_TIME;
import static uk.com.poodle.Constants.APPOINTMENT_ID;
import static uk.com.poodle.Constants.APPOINTMENT_NOTES;
import static uk.com.poodle.Constants.PATIENT_ID;
import static uk.com.poodle.data.EntityDataFactory.buildAppointmentEntity;
import static uk.com.poodle.domain.DomainDataFactory.buildAddAppointmentNotesParams;
import static uk.com.poodle.domain.DomainDataFactory.buildAddAppointmentParams;
import static uk.com.poodle.domain.DomainDataFactory.buildAppointment;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository mockRepository;

    @Mock
    private DateTimeProvider mockDateTimeProvider;

    @Mock
    private PatientServiceClient mockPatientServiceClient;

    @InjectMocks
    private AppointmentService service;

    @Test
    void shouldAddAppointment() {
        var expected = buildAppointment();
        var entity = buildAppointmentEntity();
        var params = buildAddAppointmentParams();
        var patient = Patient.builder()
            .id(PATIENT_ID)
            .build();
        when(mockPatientServiceClient.getPatient(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(mockRepository.save(entity)).thenReturn(entity.withId(APPOINTMENT_ID));

        var actual = service.addAppointment(params);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionAddingAppointmentIfPatientNotFound() {
        when(mockPatientServiceClient.getPatient(PATIENT_ID)).thenReturn(Optional.empty());

        var thrown = assertThrows(IllegalArgumentException.class, () -> service.addAppointment(buildAddAppointmentParams()));

        assertEquals("Patient not found.", thrown.getMessage());
        verifyNoInteractions(mockRepository);
    }

    @Test
    void shouldAddAppointmentNotes() {
        var expected = buildAppointment().withNotes(APPOINTMENT_NOTES);
        var entity = buildAppointmentEntity().withId(APPOINTMENT_ID);
        when(mockRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(entity));
        when(mockRepository.save(entity.withNotes(APPOINTMENT_NOTES))).thenAnswer(i -> i.getArguments()[0]);

        var actual = service.addAppointmentNotes(APPOINTMENT_ID, buildAddAppointmentNotesParams());

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionAddingAppointmentNotesIfAppointmentNotFound() {
        when(mockRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.empty());

        var thrown = assertThrows(IllegalArgumentException.class, () -> service.addAppointmentNotes(APPOINTMENT_ID, buildAddAppointmentNotesParams()));

        assertEquals("Appointment not found.", thrown.getMessage());
        verify(mockRepository, never()).save(any());
    }

    @Test
    void shouldRetrieveAllAppointments() {
        when(mockRepository.findAllByPatientId(PATIENT_ID)).thenReturn(List.of(buildAppointmentEntity(APPOINTMENT_ID)));

        var patients = service.getAppointments(PATIENT_ID, true);

        assertEquals(1, patients.size());
        assertEquals(buildAppointment(), patients.get(0));
    }

    @Test
    void shouldRetrieveAllUpcomingAppointments() {
        when(mockDateTimeProvider.getNow()).thenReturn(Optional.of(APPOINTMENT_DATE_TIME));
        when(mockRepository.findAllByPatientIdAndDateTimeGreaterThanEqual(PATIENT_ID, APPOINTMENT_DATE_TIME)).thenReturn(List.of(buildAppointmentEntity(APPOINTMENT_ID)));

        var patients = service.getAppointments(PATIENT_ID, false);

        assertEquals(1, patients.size());
        assertEquals(buildAppointment(), patients.get(0));
    }
}
