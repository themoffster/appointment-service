package uk.com.poodle.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ContactDetails.ContactDetailsBuilder.class)
public class ContactDetails {

    @JsonProperty("email")
    String email;
}
