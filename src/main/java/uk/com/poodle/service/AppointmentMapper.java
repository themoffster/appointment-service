package uk.com.poodle.service;

import lombok.experimental.UtilityClass;
import uk.com.poodle.data.AppointmentEntity;
import uk.com.poodle.domain.AddAppointmentParams;
import uk.com.poodle.domain.Appointment;

@UtilityClass
class AppointmentMapper {

    public static Appointment map(AppointmentEntity entity) {
        return Appointment.builder()
            .id(entity.getId())
            .dateTime(entity.getDateTime())
            .notes(entity.getNotes())
            .patientId(entity.getPatientId())
            .build();
    }

    public static AppointmentEntity map(AddAppointmentParams params) {
        return AppointmentEntity.builder()
            .dateTime(params.getDateTime())
            .patientId(params.getPatientId())
            .build();
    }
}
