package uk.com.poodle.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.com.poodle.domain.AddAppointmentNotesParams;
import uk.com.poodle.domain.AddAppointmentParams;
import uk.com.poodle.domain.Appointment;
import uk.com.poodle.service.AppointmentService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/appointments", produces = APPLICATION_JSON_VALUE)
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping("/add")
    public ResponseEntity<Appointment> addAppointment(@RequestBody @Valid AddAppointmentParams params) {
        return ResponseEntity.status(CREATED).body(service.addAppointment(params));
    }

    @PostMapping("/{appointmentId}/notes/add")
    public ResponseEntity<Appointment> addAppointmentNotes(
        @PathVariable("appointmentId") String appointmentId,
        @RequestBody @Valid AddAppointmentNotesParams params) {
        return ResponseEntity.status(CREATED).body(service.addAppointmentNotes(appointmentId, params));
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments(@RequestParam("patientId") String patientId, @RequestParam(defaultValue = "false") boolean includeHistoric) {
        return ok(service.getAppointments(patientId, includeHistoric));
    }
}
