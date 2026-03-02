package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.config.SampleThresholdProperties;
import buralek.assignment.ground.application.dto.SampleStatisticFilterDto;
import buralek.assignment.ground.application.dto.SampleStatisticResponse;
import buralek.assignment.ground.application.dto.SampleSurpassingThresholdDto;
import buralek.assignment.ground.application.dto.SampleThresholdDto;
import buralek.assignment.ground.domain.model.SampleStatistics;
import buralek.assignment.ground.domain.model.Threshold;
import buralek.assignment.ground.domain.service.StatisticDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticApplicationServiceImpl implements StatisticApplicationService {
    private final StatisticDomainService statisticDomainService;
    private final SampleThresholdProperties thresholdProperties;

    @Transactional(readOnly = true)
    public SampleStatisticResponse getStatisticSample(UUID locationId) {
        Threshold threshold = Threshold.builder()
                .unitWeight(thresholdProperties.getUnitWeight())
                .waterContent(thresholdProperties.getWaterContent())
                .shearStrength(thresholdProperties.getShearStrength())
                .build();

        SampleStatistics statistics = statisticDomainService.calculate(locationId, threshold);

        return SampleStatisticResponse.builder()
                .filter(SampleStatisticFilterDto.builder().locationId(locationId).build())
                .averageWaterContent(statistics.getAverageWaterContent())
                .totalSamples(statistics.getTotalSamples())
                .samplesSurpassingThreshold(SampleSurpassingThresholdDto.builder()
                        .unitWeight(statistics.getUnitWeightExceeding())
                        .waterContent(statistics.getWaterContentExceeding())
                        .shearStrength(statistics.getShearStrengthExceeding())
                        .build())
                .thresholds(SampleThresholdDto.builder()
                        .unitWeight(thresholdProperties.getUnitWeight())
                        .waterContent(thresholdProperties.getWaterContent())
                        .shearStrength(thresholdProperties.getShearStrength())
                        .build())
                .build();
    }
}