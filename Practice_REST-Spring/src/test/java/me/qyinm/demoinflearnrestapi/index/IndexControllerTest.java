package me.qyinm.demoinflearnrestapi.index;

import me.qyinm.demoinflearnrestapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IndexControllerTest extends BaseControllerTest {

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());
    }
}
