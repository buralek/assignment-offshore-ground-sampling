package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.model.SampleStatistics;
import buralek.assignment.ground.domain.model.Threshold;
import buralek.assignment.ground.domain.port.SampleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticDomainServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @InjectMocks
    private StatisticDomainService statisticDomainService;

    private static final UUID ID_1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    private static final UUID ID_2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");

    private Sample buildSample(UUID id, double unitWeight, double waterContent, double shearStrength) {
        return Sample.builder()
                .id(id)
                .unitWeight(unitWeight)
                .waterContent(waterContent)
                .shearStrength(shearStrength)
                .build();
    }

    private Threshold buildThreshold() {
        return Threshold.builder()
                .unitWeight(25.0)
                .waterContent(100.0)
                .shearStrength(800.0)
                .build();
    }
    
    @Test
    @DisplayName("WHEN no samples are provided, THEN averageWaterContent is 0.0, totalSamples is 0, and all exceeding lists are empty")
    void calculate1() {
        when(sampleRepository.findAll()).thenReturn(List.of());

        SampleStatistics result = statisticDomainService.calculate(null, buildThreshold());

        assertThat(result).isEqualTo(SampleStatistics.builder()
                .averageWaterContent(0.0)
                .totalSamples(0)
                .unitWeightExceeding(List.of())
                .waterContentExceeding(List.of())
                .shearStrengthExceeding(List.of())
                .build());
    }

    @Test
    @DisplayName("WHEN multiple samples are provided, THEN averageWaterContent is the arithmetic mean and totalSamples reflects the count")
    void calculate2() {
        List<Sample> samples = List.of(
                buildSample(ID_1, 20.0, 60.0, 400.0),
                buildSample(ID_2, 22.0, 80.0, 600.0)
        );
        when(sampleRepository.findAll()).thenReturn(samples);

        SampleStatistics result = statisticDomainService.calculate(null, buildThreshold());

        assertThat(result.getAverageWaterContent()).isEqualTo(70.0);
        assertThat(result.getTotalSamples()).isEqualTo(2);
    }

    @Test
    @DisplayName("WHEN a sample exceeds the unit weight threshold, THEN its id appears in unitWeightExceeding and others do not")
    void calculate3() {
        List<Sample> samples = List.of(
                buildSample(ID_1, 26.0, 50.0, 400.0),   // unitWeight 26 > 25 → exceeds
                buildSample(ID_2, 20.0, 50.0, 400.0)    // unitWeight 20 < 25 → does not exceed
        );
        when(sampleRepository.findAll()).thenReturn(samples);

        SampleStatistics result = statisticDomainService.calculate(null, buildThreshold());

        assertThat(result.getUnitWeightExceeding()).containsExactly(ID_1);
    }

    @Test
    @DisplayName("WHEN a sample exceeds the water content threshold, THEN its id appears in waterContentExceeding and others do not")
    void calculate4() {
        List<Sample> samples = List.of(
                buildSample(ID_1, 20.0, 110.0, 400.0),  // waterContent 110 > 100 → exceeds
                buildSample(ID_2, 20.0, 90.0, 400.0)    // waterContent 90 < 100 → does not exceed
        );
        when(sampleRepository.findAll()).thenReturn(samples);

        SampleStatistics result = statisticDomainService.calculate(null, buildThreshold());

        assertThat(result.getWaterContentExceeding()).containsExactly(ID_1);
    }

    @Test
    @DisplayName("WHEN a sample exceeds the shear strength threshold, THEN its id appears in shearStrengthExceeding and others do not")
    void calculate5() {
        List<Sample> samples = List.of(
                buildSample(ID_1, 20.0, 50.0, 900.0),   // shearStrength 900 > 800 → exceeds
                buildSample(ID_2, 20.0, 50.0, 600.0)    // shearStrength 600 < 800 → does not exceed
        );
        when(sampleRepository.findAll()).thenReturn(samples);

        SampleStatistics result = statisticDomainService.calculate(null, buildThreshold());

        assertThat(result.getShearStrengthExceeding()).containsExactly(ID_1);
    }

    @Test
    @DisplayName("WHEN a sample is exactly at a threshold value, THEN it does NOT appear in the exceeding list")
    void calculate6() {
        List<Sample> samples = List.of(
                buildSample(ID_1, 25.0, 100.0, 800.0)   // all values exactly at threshold
        );
        when(sampleRepository.findAll()).thenReturn(samples);

        SampleStatistics result = statisticDomainService.calculate(null, buildThreshold());

        assertThat(result.getUnitWeightExceeding()).isEmpty();
        assertThat(result.getWaterContentExceeding()).isEmpty();
        assertThat(result.getShearStrengthExceeding()).isEmpty();
    }
}