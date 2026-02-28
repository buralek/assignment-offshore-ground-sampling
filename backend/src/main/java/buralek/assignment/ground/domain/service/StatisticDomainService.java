package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.model.SampleStatistics;
import buralek.assignment.ground.domain.model.Threshold;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StatisticDomainService {

    public SampleStatistics calculate(List<Sample> samples, Threshold threshold) {
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

        return SampleStatistics.builder()
                .averageWaterContent(averageWaterContent)
                .totalSamples(samples.size())
                .unitWeightExceeding(unitWeightExceeding)
                .waterContentExceeding(waterContentExceeding)
                .shearStrengthExceeding(shearStrengthExceeding)
                .build();
    }
}