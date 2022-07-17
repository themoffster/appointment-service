package uk.com.poodle.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Guardian.GuardianBuilder.class)
public class Guardian {

    @JsonProperty("id")
    String id;

    @JsonProperty("contactDetails")
    ContactDetails contactDetails;
}
