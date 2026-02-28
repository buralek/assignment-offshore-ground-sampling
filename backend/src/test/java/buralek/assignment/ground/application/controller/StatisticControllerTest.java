package buralek.assignment.ground.application.controller;

import buralek.assignment.ground.application.dto.SampleStatisticFilterDto;
import buralek.assignment.ground.application.dto.SampleStatisticResponse;
import buralek.assignment.ground.application.dto.SampleSurpassingThresholdDto;
import buralek.assignment.ground.application.service.StatisticApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticController.class)
class StatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatisticApplicationService statisticApplicationService;

    private static final UUID LOCATION_ID = UUID.fromString("661f9511-f3a2-52e5-b827-557766551111");
    private static final UUID SAMPLE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    private SampleStatisticResponse buildResponse(UUID locationId) {
        return SampleStatisticResponse.builder()
                .filter(SampleStatisticFilterDto.builder().locationId(locationId).build())
                .averageWaterContent(55.2)
                .totalSamples(1)
                .samplesSurpassingThreshold(SampleSurpassingThresholdDto.builder()
                        .unitWeight(List.of(SAMPLE_ID))
                        .waterContent(List.of())
                        .shearStrength(List.of())
                        .build())
                .build();
    }

    @Test
    @DisplayName("WHEN calling getSampleStatistic without a locationId, THEN statistics for all samples are returned with a null filter")
    void getSampleStatistic1() throws Exception {
        when(statisticApplicationService.getStatisticSample(null)).thenReturn(buildResponse(null));

        mockMvc.perform(get("/api/v1/statistics/samples"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "filter": { "locationId": null },
                          "averageWaterContent": 55.2,
                          "totalSamples": 1,
                          "samplesSurpassingThreshold": {
                            "unitWeight": ["%s"],
                            "waterContent": [],
                            "shearStrength": []
                          }
                        }
                        """.formatted(SAMPLE_ID)));
    }

    @Test
    @DisplayName("WHEN calling getSampleStatistic with a locationId, THEN filtered statistics are returned with the locationId echoed in the filter")
    void getSampleStatistic2() throws Exception {
        when(statisticApplicationService.getStatisticSample(LOCATION_ID)).thenReturn(buildResponse(LOCATION_ID));

        mockMvc.perform(get("/api/v1/statistics/samples")
                        .param("locationId", LOCATION_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "filter": { "locationId": "%s" },
                          "averageWaterContent": 55.2,
                          "totalSamples": 1,
                          "samplesSurpassingThreshold": {
                            "unitWeight": ["%s"],
                            "waterContent": [],
                            "shearStrength": []
                          }
                        }
                        """.formatted(LOCATION_ID, SAMPLE_ID)));
    }
}