package buralek.assignment.ground.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@Schema(description = "Statistic sample filter fields used in the request")
public class SampleStatisticFilterDto {
    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    UUID locationId;
}
