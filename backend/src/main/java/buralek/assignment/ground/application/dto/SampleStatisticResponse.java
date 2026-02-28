package buralek.assignment.ground.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Statistic information")
public class SampleStatisticResponse {
    @Schema
    SampleStatisticFilterDto filter;

    @Schema(example = "50.5")
    Double averageWaterContent;

    @Schema(example = "10")
    Integer totalSamples;

    @Schema
    SampleSurpassingThresholdDto samplesSurpassingThreshold;
}