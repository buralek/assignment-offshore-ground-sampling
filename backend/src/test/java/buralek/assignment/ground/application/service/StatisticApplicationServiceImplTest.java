package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.config.SampleThresholdProperties;
import buralek.assignment.ground.application.dto.SampleStatisticFilterDto;
import buralek.assignment.ground.application.dto.SampleStatisticResponse;
import buralek.assignment.ground.application.dto.SampleSurpassingThresholdDto;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.model.SampleStatistics;
import buralek.assignment.ground.domain.model.Threshold;
import buralek.assignment.ground.domain.port.SampleRepository;
import buralek.assignment.ground.domain.service.StatisticDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticApplicationServiceImplTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private StatisticDomainService statisticDomainService;

    @Mock
    private SampleThresholdProperties thresholdProperties;

    @InjectMocks
    private StatisticApplicationServiceImpl statisticApplicationService;

    private static final UUID LOCATION_ID = UUID.fromString("661f9511-f3a2-52e5-b827-557766551111");
    private static final UUID SAMPLE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    private Sample buildSample() {
        return Sample.builder()
                .id(SAMPLE_ID)
                .unitWeight(18.7)
                .waterContent(22.4)
                .shearStrength(35.8)
                .build();
    }

    private SampleStatistics buildStatistics() {
        return SampleStatistics.builder()
                .averageWaterContent(22.4)
                .totalSamples(1)
                .unitWeightExceeding(List.of())
                .waterContentExceeding(List.of())
                .shearStrengthExceeding(List.of())
                .build();
    }

    private void stubThresholdProperties() {
        when(thresholdProperties.getUnitWeight()).thenReturn(25.0);
        when(thresholdProperties.getWaterContent()).thenReturn(100.0);
        when(thresholdProperties.getShearStrength()).thenReturn(800.0);
    }

    @Test
    @DisplayName("WHEN locationId is null, THEN sampleRepository.findAll() is called and findAllByLocationId() is never called")
    void getStatisticSample1() {
        stubThresholdProperties();
        when(sampleRepository.findAll()).thenReturn(List.of(buildSample()));
        when(statisticDomainService.calculate(any(), any())).thenReturn(buildStatistics());

        statisticApplicationService.getStatisticSample(null);

        verify(sampleRepository).findAll();
        verify(sampleRepository, never()).findAllByLocationId(any());
    }

    @Test
    @DisplayName("WHEN locationId is provided, THEN sampleRepository.findAllByLocationId() is called with that id and findAll() is never called")
    void getStatisticSample2() {
        stubThresholdProperties();
        when(sampleRepository.findAllByLocationId(LOCATION_ID)).thenReturn(List.of(buildSample()));
        when(statisticDomainService.calculate(any(), any())).thenReturn(buildStatistics());

        statisticApplicationService.getStatisticSample(LOCATION_ID);

        verify(sampleRepository).findAllByLocationId(LOCATION_ID);
        verify(sampleRepository, never()).findAll();
    }

    @Test
    @DisplayName("WHEN called, THEN threshold values from properties are forwarded to the domain service and the result is correctly mapped to the response")
    void getStatisticSample3() {
        SampleStatistics statistics = SampleStatistics.builder()
                .averageWaterContent(22.4)
                .totalSamples(1)
                .unitWeightExceeding(List.of(SAMPLE_ID))
                .waterContentExceeding(List.of())
                .shearStrengthExceeding(List.of())
                .build();

        when(thresholdProperties.getUnitWeight()).thenReturn(25.0);
        when(thresholdProperties.getWaterContent()).thenReturn(100.0);
        when(thresholdProperties.getShearStrength()).thenReturn(800.0);
        when(sampleRepository.findAll()).thenReturn(List.of(buildSample()));
        when(statisticDomainService.calculate(any(), any())).thenReturn(statistics);

        SampleStatisticResponse result = statisticApplicationService.getStatisticSample(null);

        ArgumentCaptor<Threshold> thresholdCaptor = ArgumentCaptor.forClass(Threshold.class);
        verify(statisticDomainService).calculate(any(), thresholdCaptor.capture());

        Threshold capturedThreshold = thresholdCaptor.getValue();
        assertThat(capturedThreshold.getUnitWeight()).isEqualTo(25.0);
        assertThat(capturedThreshold.getWaterContent()).isEqualTo(100.0);
        assertThat(capturedThreshold.getShearStrength()).isEqualTo(800.0);

        assertThat(result).isEqualTo(SampleStatisticResponse.builder()
                .filter(SampleStatisticFilterDto.builder().locationId(null).build())
                .averageWaterContent(22.4)
                .totalSamples(1)
                .samplesSurpassingThreshold(SampleSurpassingThresholdDto.builder()
                        .unitWeight(List.of(SAMPLE_ID))
                        .waterContent(List.of())
                        .shearStrength(List.of())
                        .build())
                .build());
    }
}