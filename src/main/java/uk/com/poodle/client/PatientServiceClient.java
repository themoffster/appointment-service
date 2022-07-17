package uk.com.poodle.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.com.poodle.client.domain.Patient;

import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Component
public class PatientServiceClient {

    private final RestTemplate restTemplate;

    @Autowired
    public PatientServiceClient(RestTemplateBuilder builder, PatientServiceClientConfig config) {
        this.restTemplate = builder
            .rootUri(config.getBaseUrl())
            .setConnectTimeout(config.getConnectTimeout())
            .setReadTimeout(config.getReadTimeout())
            .build();
    }

    public Optional<Patient> getPatient(String patientId) {
        Map<String, Object> uriVariables = Map.of("id", patientId);
        var uriTemplate =
            UriComponentsBuilder.fromUriString("/patients/{id}").build();
        log.debug("Retrieving patient {}.", uriTemplate.expand(uriVariables).toUriString());

        var response = restTemplate.exchange(uriTemplate.toUriString(), GET, new HttpEntity<>(new HttpHeaders()), Patient.class, uriVariables);
        return Optional.ofNullable(response.getBody());
    }
}
