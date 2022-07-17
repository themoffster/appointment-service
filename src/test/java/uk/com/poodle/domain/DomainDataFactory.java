package uk.com.poodle.domain;

import static uk.com.poodle.Constants.APPOINTMENT_DATE_TIME;
import static uk.com.poodle.Constants.APPOINTMENT_ID;
import static uk.com.poodle.Constants.APPOINTMENT_NOTES;
import static uk.com.poodle.Constants.PATIENT_ID;

public class DomainDataFactory {

    public static Appointment buildAppointment() {
        return Appointment.builder()
            .id(APPOINTMENT_ID)
            .dateTime(APPOINTMENT_DATE_TIME)
            .patientId(PATIENT_ID)
            .build();
    }

    public static AddAppointmentParams buildAddAppointmentParams() {
        return AddAppointmentParams.builder()
            .dateTime(APPOINTMENT_DATE_TIME)
            .patientId(PATIENT_ID)
            .build();
    }

    public static AddAppointmentNotesParams buildAddAppointmentNotesParams() {
        return AddAppointmentNotesParams.builder()
            .notes(APPOINTMENT_NOTES)
            .build();
    }
}
