package uk.com.poodle.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.com.poodle.Constants.APPOINTMENT_ID;
import static uk.com.poodle.data.EntityDataFactory.buildAppointmentEntity;
import static uk.com.poodle.domain.DomainDataFactory.buildAddAppointmentParams;
import static uk.com.poodle.domain.DomainDataFactory.buildAppointment;
import static uk.com.poodle.service.AppointmentMapper.map;

class AppointmentMapperTest {

    @Test
    void classCanNotBeInstantiated() throws NoSuchMethodException {
        var constructor = AppointmentMapper.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void shouldMapAppointmentEntity() {
        var appointment = map(buildAppointmentEntity(APPOINTMENT_ID));
        assertEquals(buildAppointment(), appointment);
    }

    @Test
    void shouldMapAddAppointmentParams() {
        var appointment = map(buildAddAppointmentParams());
        assertEquals(buildAppointmentEntity(), appointment);
    }
}
