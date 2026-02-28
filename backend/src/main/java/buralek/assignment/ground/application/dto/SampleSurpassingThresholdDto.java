package buralek.assignment.ground.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@Schema(description = "Lists of samples surpassing different thresholds")
public class SampleSurpassingThresholdDto {
    List<UUID> unitWeight;
    List<UUID> waterContent;
    List<UUID> shearStrength;
}
