package uk.com.poodle.docs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(excludeFilters = {@Filter(value = {RestController.class})})
class DocumentationConfigTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldRedirectToSwaggerFromRootUrl() throws Exception {
        mvc.perform(get("/"))
            .andExpect(status().isFound())
            .andExpect(header().string(LOCATION, "/swagger-ui.html"));
    }
}
