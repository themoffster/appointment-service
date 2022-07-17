package uk.com.poodle.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonDeserialize(builder = Patient.PatientBuilder.class)
public class Patient {

    @JsonProperty("id")
    String id;

    @JsonProperty("guardians")
    List<Guardian> guardians;
}
