package uk.com.poodle.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Value
@Builder
@Jacksonized
public class AddAppointmentParams {

    @NotBlank
    String patientId;

    @Future
    @NotNull
    @JsonFormat(shape = STRING)
    LocalDateTime dateTime;
}
