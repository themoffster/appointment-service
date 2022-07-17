package uk.com.poodle.data;

import static uk.com.poodle.Constants.APPOINTMENT_DATE_TIME;
import static uk.com.poodle.Constants.PATIENT_ID;

public class EntityDataFactory {

    public static AppointmentEntity buildAppointmentEntity() {
        return buildAppointmentEntity(null);
    }

    public static AppointmentEntity buildAppointmentEntity(String id) {
        return AppointmentEntity.builder()
            .id(id)
            .dateTime(APPOINTMENT_DATE_TIME)
            .patientId(PATIENT_ID)
            .build();
    }
}
