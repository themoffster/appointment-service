package uk.com.poodle.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.json.JSONException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.com.poodle.config.EmbeddedDatabaseTestConfig;
import uk.com.poodle.domain.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.request;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD;
import static java.util.Arrays.asList;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.com.poodle.Constants.APPOINTMENT_DATE_TIME;
import static uk.com.poodle.Constants.APPOINTMENT_ID;
import static uk.com.poodle.Constants.APPOINTMENT_NOTES;
import static uk.com.poodle.Constants.PATIENT_ID;
import static uk.com.poodle.domain.DomainDataFactory.buildAddAppointmentNotesParams;
import static uk.com.poodle.domain.DomainDataFactory.buildAddAppointmentParams;
import static uk.com.poodle.domain.DomainDataFactory.buildAppointment;

@AutoConfigureWireMock(port = 0)
@ExtendWith(SpringExtension.class)
@Import(EmbeddedDatabaseTestConfig.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureEmbeddedDatabase(provider = ZONKY, refresh = AFTER_EACH_TEST_METHOD)
class AppointmentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    protected WireMockServer wireMockServer;

    @TestWithData
    void shouldAddAppointment() throws JsonProcessingException, JSONException {
        var payload = buildAddAppointmentParams();
        stubPatientServiceClient();

        var responseEntity = restTemplate.postForEntity("/appointments/add", payload, Appointment.class);

        assertEquals(CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        JSONAssert.assertEquals(
            mapper.writeValueAsString(buildAppointment()),
            mapper.writeValueAsString(responseEntity.getBody()),
            new CustomComparator(
                JSONCompareMode.STRICT,
                new Customization("id", (o1, o2) -> true)));
    }

    @TestWithData
    void shouldAddAppointmentNotes() {
        var urlTemplate = "/appointments/{appointmentId}/notes/add";
        var payload = buildAddAppointmentNotesParams();
        var urlParams = Map.of(
            "appointmentId", APPOINTMENT_ID
        );

        var responseEntity = restTemplate.postForEntity(urlTemplate, payload, Appointment.class, urlParams);

        var expected = Appointment.builder()
            .id(APPOINTMENT_ID)
            .dateTime(APPOINTMENT_DATE_TIME)
            .notes(APPOINTMENT_NOTES)
            .patientId(PATIENT_ID)
            .build();

        assertEquals(CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expected, responseEntity.getBody());
    }

    @TestWithData
    void shouldRetrieveAllAppointmentsForPatient() {
        var urlTemplate = "/appointments?patientId=" + PATIENT_ID + "&includeHistoric=true";
        var responseEntity = restTemplate.getForEntity(urlTemplate, Appointment[].class);

        var expected = List.of(
            Appointment.builder()
                .id(APPOINTMENT_ID)
                .patientId(PATIENT_ID)
                .dateTime(APPOINTMENT_DATE_TIME)
                .build(),
            Appointment.builder()
                .id("cf6c5228-31b4-411d-9cb7-f6ec73b7fee8")
                .patientId(PATIENT_ID)
                .dateTime(LocalDateTime.of(2022, 1, 1, 9, 0))
                .build()
        );

        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().length);
        assertTrue(expected.containsAll(asList(responseEntity.getBody())));
    }

    @TestWithData
    void shouldRetrieveAllUpcomingAppointmentsForPatient() {
        var urlTemplate = "/appointments?patientId=" + PATIENT_ID;
        var responseEntity = restTemplate.getForEntity(urlTemplate, Appointment[].class);

        var expected = Appointment.builder()
            .id(APPOINTMENT_ID)
            .patientId(PATIENT_ID)
            .dateTime(APPOINTMENT_DATE_TIME)
            .build();

        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().length);
        assertEquals(expected, responseEntity.getBody()[0]);
    }

    private void stubPatientServiceClient() {
        stubFor(request(GET.name(), urlEqualTo("/patients/" + PATIENT_ID))
            .willReturn(aResponse()
                .withBody("""
                    {
                        "id": "67150f64-b235-4e07-af6b-4018aabc1e3e"
                    }
                    """)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .withStatus(OK.value())));
    }
}
