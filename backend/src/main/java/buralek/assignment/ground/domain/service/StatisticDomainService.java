package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.model.SampleStatistics;
import buralek.assignment.ground.domain.model.Threshold;
import buralek.assignment.ground.domain.port.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticDomainService {
    private final SampleRepository sampleRepository;

    public SampleStatistics calculate(UUID locationId, Threshold threshold) {
        log.info("Calculating statistics for location [{}] with threshold [{}]", locationId, threshold);
        List<Sample> samples = locationId != null
                ? sampleRepository.findAllByLocationId(locationId)
                : sampleRepository.findAll();

        double averageWaterContent = samples.stream()
                .mapToDouble(Sample::getWaterContent)
                .average()
                .orElse(0.0);

        List<UUID> unitWeightExceeding = samples.stream()
                .filter(s -> s.getUnitWeight() > threshold.getUnitWeight())
                .map(Sample::getId)
                .toList();

        List<UUID> waterContentExceeding = samples.stream()
                .filter(s -> s.getWaterContent() > threshold.getWaterContent())
                .map(Sample::getId)
                .toList();

        List<UUID> shearStrengthExceeding = samples.stream()
                .filter(s -> s.getShearStrength() > threshold.getShearStrength())
                .map(Sample::getId)
                .toList();

        log.info("Sample statistics: " +
                        "average watter content [{}], " +
                        "unit weight exceeding samples[{}], " +
                        "water content exceeding samples [{}], " +
                        "shear strength exceeding samples [{}] ",
                averageWaterContent,
                unitWeightExceeding.size(),
                waterContentExceeding.size(),
                shearStrengthExceeding.size());

        return SampleStatistics.builder()
                .averageWaterContent(averageWaterContent)
                .totalSamples(samples.size())
                .unitWeightExceeding(unitWeightExceeding)
                .waterContentExceeding(waterContentExceeding)
                .shearStrengthExceeding(shearStrengthExceeding)
                .build();
    }
}