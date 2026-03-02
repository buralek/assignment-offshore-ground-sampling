package buralek.assignment.ground.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Configured threshold values for sample measurements (SI units)")
public class SampleThresholdDto {
    @Schema(example = "25.0")  double unitWeight;
    @Schema(example = "100.0") double waterContent;
    @Schema(example = "800.0") double shearStrength;
}