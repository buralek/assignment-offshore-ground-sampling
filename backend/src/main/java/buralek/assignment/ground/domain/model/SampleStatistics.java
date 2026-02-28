package buralek.assignment.ground.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class SampleStatistics {
    double averageWaterContent;
    int totalSamples;
    List<UUID> unitWeightExceeding;
    List<UUID> waterContentExceeding;
    List<UUID> shearStrengthExceeding;
}