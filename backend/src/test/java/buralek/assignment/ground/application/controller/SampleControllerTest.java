package buralek.assignment.ground.application.controller;

import buralek.assignment.ground.application.dto.SampleCursor;
import buralek.assignment.ground.application.dto.SamplePageResponse;
import buralek.assignment.ground.application.dto.SampleResponse;
import buralek.assignment.ground.application.service.SampleApplicationService;
import buralek.assignment.ground.domain.exception.SampleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SampleController.class)
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SampleApplicationService sampleApplicationService;

    private static final UUID SAMPLE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID LOCATION_ID = UUID.fromString("661f9511-f3a2-52e5-b827-557766551111");
    private static final String SAMPLING_DATE = "2026-07-01T10:15:30+02:00";      // response (OffsetDateTime)
    private static final String SAMPLING_INSTANT = "2026-07-01T08:15:30Z";        // request (Instant)

    private SampleResponse buildSampleResponse() {
        return SampleResponse.builder()
                .id(SAMPLE_ID)
                .locationId(LOCATION_ID)
                .timestampWithTimeZone(OffsetDateTime.parse(SAMPLING_DATE))
                .unitWeight(18.7)
                .waterContent(22.4)
                .shearStrength(35.8)
                .build();
    }

    private String validRequestJson() {
        return """
                {
                  "locationId": "%s",
                  "samplingTimestamp": "%s",
                  "unitWeight": 18.7,
                  "waterContent": 22.4,
                  "shearStrength": 35.8
                }
                """.formatted(LOCATION_ID, SAMPLING_INSTANT);
    }

    private String expectedResponseJson() {
        return """
                {
                  "id": "%s",
                  "locationId": "%s",
                  "timestampWithTimeZone": "%s",
                  "unitWeight": 18.7,
                  "waterContent": 22.4,
                  "shearStrength": 35.8
                }
                """.formatted(SAMPLE_ID, LOCATION_ID, SAMPLING_DATE);
    }

    // ── POST ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling createSample endpoint with a valid request, THEN the created sample is returned with 201")
    void createSample1() throws Exception {
        when(sampleApplicationService.createSample(any())).thenReturn(buildSampleResponse());

        mockMvc.perform(post("/api/v1/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponseJson()));
    }

    @Test
    @DisplayName("WHEN calling createSample endpoint with an invalid request, THEN 400 is returned")
    void createSample2() throws Exception {
        String invalidRequest = """
                {
                  "unitWeight": -1.0
                }
                """;

        mockMvc.perform(post("/api/v1/samples")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().is4xxClientError());
    }

    // ── GET by id ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling getSampleById endpoint, THEN the sample with that id is returned")
    void getSampleById1() throws Exception {
        when(sampleApplicationService.getSampleById(SAMPLE_ID)).thenReturn(buildSampleResponse());

        mockMvc.perform(get("/api/v1/samples/{id}", SAMPLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseJson()));
    }

    @Test
    @DisplayName("WHEN calling getSampleById endpoint and the sample is not found, THEN 404 is returned")
    void getSampleById2() throws Exception {
        when(sampleApplicationService.getSampleById(SAMPLE_ID))
                .thenThrow(new SampleNotFoundException(SAMPLE_ID));

        mockMvc.perform(get("/api/v1/samples/{id}", SAMPLE_ID))
                .andExpect(status().isNotFound());
    }

    // ── PUT ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling updateSample endpoint with a valid request, THEN the updated sample is returned")
    void updateSample1() throws Exception {
        when(sampleApplicationService.updateSample(eq(SAMPLE_ID), any())).thenReturn(buildSampleResponse());

        mockMvc.perform(put("/api/v1/samples/{id}", SAMPLE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseJson()));
    }

    @Test
    @DisplayName("WHEN calling updateSample endpoint and the sample is not found, THEN 404 is returned")
    void updateSample2() throws Exception {
        when(sampleApplicationService.updateSample(eq(SAMPLE_ID), any()))
                .thenThrow(new SampleNotFoundException(SAMPLE_ID));

        mockMvc.perform(put("/api/v1/samples/{id}", SAMPLE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("WHEN calling updateSample endpoint with an invalid request, THEN 400 is returned")
    void updateSample3() throws Exception {
        String invalidRequest = """
                {
                  "unitWeight": -5.0
                }
                """;

        mockMvc.perform(put("/api/v1/samples/{id}", SAMPLE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().is4xxClientError());
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling deleteSample endpoint, THEN 204 No Content is returned")
    void deleteSample1() throws Exception {
        doNothing().when(sampleApplicationService).deleteSample(SAMPLE_ID);

        mockMvc.perform(delete("/api/v1/samples/{id}", SAMPLE_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("WHEN calling deleteSample endpoint and the sample is not found, THEN 404 is returned")
    void deleteSample2() throws Exception {
        doThrow(new SampleNotFoundException(SAMPLE_ID))
                .when(sampleApplicationService).deleteSample(SAMPLE_ID);

        mockMvc.perform(delete("/api/v1/samples/{id}", SAMPLE_ID))
                .andExpect(status().isNotFound());
    }

    // ── GET /page ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling getSamplesPage with no cursor, THEN first page is returned with hasMore=false and no nextCursor")
    void getSamplesPage1() throws Exception {
        SamplePageResponse pageResponse = SamplePageResponse.builder()
                .data(List.of(buildSampleResponse()))
                .hasMore(false)
                .nextCursor(null)
                .build();

        when(sampleApplicationService.getSamplesPage(null, null, null, 20)).thenReturn(pageResponse);

        String expectedJson = """
                {
                  "data": [
                    {
                      "id": "%s",
                      "locationId": "%s",
                      "timestampWithTimeZone": "%s",
                      "unitWeight": 18.7,
                      "waterContent": 22.4,
                      "shearStrength": 35.8
                    }
                  ],
                  "hasMore": false
                }
                """.formatted(SAMPLE_ID, LOCATION_ID, SAMPLING_DATE);

        mockMvc.perform(get("/api/v1/samples/page"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("WHEN calling getSamplesPage with cursor params, THEN next page is returned with hasMore=true and nextCursor")
    void getSamplesPage2() throws Exception {
        Instant afterTimestamp = Instant.parse(SAMPLING_INSTANT);
        SampleCursor nextCursor = SampleCursor.builder()
                .afterTimestamp(afterTimestamp)
                .afterId(SAMPLE_ID)
                .build();
        SamplePageResponse pageResponse = SamplePageResponse.builder()
                .data(List.of(buildSampleResponse()))
                .hasMore(true)
                .nextCursor(nextCursor)
                .build();

        when(sampleApplicationService.getSamplesPage(null, afterTimestamp, SAMPLE_ID, 20)).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/samples/page")
                        .param("afterTimestamp", SAMPLING_INSTANT)
                        .param("afterId", SAMPLE_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "hasMore": true,
                          "nextCursor": {
                            "afterTimestamp": "%s",
                            "afterId": "%s"
                          }
                        }
                        """.formatted(SAMPLING_INSTANT, SAMPLE_ID)));
    }

    @Test
    @DisplayName("WHEN calling getSamplesPage with a limit exceeding the maximum, THEN 400 is returned")
    void getSamplesPage3() throws Exception {
        mockMvc.perform(get("/api/v1/samples/page")
                        .param("limit", "101"))
                .andExpect(status().is4xxClientError());
    }
}