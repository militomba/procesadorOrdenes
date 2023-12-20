package ar.edu.um.programacion2.procesadorordenes.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.um.programacion2.procesadorordenes.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Test class for the ReportesResource REST controller.
 *
 * @see ReportesResource
 */
@IntegrationTest
class ReportesResourceIT {

    private MockMvc restMockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ReportesResource reportesResource = new ReportesResource();
        restMockMvc = MockMvcBuilders.standaloneSetup(reportesResource).build();
    }

    /**
     * Test defaultAction
     */
    @Test
    void testDefaultAction() throws Exception {
        restMockMvc.perform(get("/api/reportes-resource/default-action")).andExpect(status().isOk());
    }
}
