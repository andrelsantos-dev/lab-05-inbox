package com.alssant.asclepio;

import com.alssant.asclepio.patient.dto.PatientResponse;
import com.alssant.asclepio.patient.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class Lab05InboxApplicationTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    PatientRepository repository;

    @Test
    void contextLoads() {
    }

    @Test
    void testPatientCreation() throws Exception {
        final String patientName = "Charlie";
        final UUID tenantId = UUID.randomUUID();

        PatientResponse response = createPatient(tenantId.toString(), patientName);
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(patientName);
        assertThat(response.tenantId()).isEqualTo(tenantId);
        assertThat(repository.findAll())
                .hasSize(1);
    }


    protected PatientResponse createPatient(String tenant, String patientName) throws Exception {
        String content = "{\"name\": \"%s\"}".formatted(patientName);

        MvcResult result = mockMvc.perform(post("/patients")
                        .header("X-Tenant-Id", tenant)
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andReturn();

        return mapper.readValue(
                result.getResponse().getContentAsString(),
                PatientResponse.class
        );
    }


}
