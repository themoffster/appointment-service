package uk.com.poodle.data;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static uk.com.poodle.Constants.APPOINTMENT_DATE_TIME;
import static uk.com.poodle.Constants.PATIENT_ID;
import static uk.com.poodle.data.EntityDataFactory.buildAppointmentEntity;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Mock
    private DateTimeProvider mockDateTimeProvider;

    @Test
    void shouldFindAllAppointmentsByPatientId() {
        var entity = buildAppointmentEntity();
        appointmentRepository.save(entity);

        var appointments = appointmentRepository.findAllByPatientId(PATIENT_ID);

        assertEquals(1, appointments.size());
        assertTrue(appointments.contains(entity));
    }

    @Test
    void shouldFindAllUpcomingAppointmentsByPatientId() {
        var historicAppointmentEntity = buildAppointmentEntity().toBuilder()
            .dateTime(LocalDateTime.of(2022, 1, 1, 9, 0))
            .build();
        var upcomingAppointmentEntity = buildAppointmentEntity();
        appointmentRepository.saveAll(List.of(historicAppointmentEntity, upcomingAppointmentEntity));
        when(mockDateTimeProvider.getNow()).thenReturn(Optional.of(APPOINTMENT_DATE_TIME));

        var appointments = appointmentRepository.findAllByPatientIdAndDateTimeGreaterThanEqual(PATIENT_ID, APPOINTMENT_DATE_TIME);

        assertEquals(1, appointments.size());
        assertEquals(upcomingAppointmentEntity, appointments.get(0));
    }

    @Test
    void shouldFindAllByDateTimeAfter() {
        var appointment = buildAppointmentEntity();
        appointmentRepository.save(appointment);

        var appointments = appointmentRepository.findAllByDateTimeAfter(APPOINTMENT_DATE_TIME.minusDays(1L));

        assertEquals(1, appointments.size());
        assertEquals(appointment, appointments.get(0));
    }
}
