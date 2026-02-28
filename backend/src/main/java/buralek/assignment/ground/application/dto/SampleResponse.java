package buralek.assignment.ground.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
@Schema(description = "Sample information")
public class SampleResponse {
    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id;

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    UUID locationId;

    @Schema(description = "Sampling timestamp with timezone",
            example = "2026-07-01T10:15:30+02:00")
    OffsetDateTime timestampWithTimeZone;

    @Schema(example = "18.7")
    Double unitWeight;

    @Schema(example = "22.4")
    Double waterContent;

    @Schema(example = "35.8")
    Double shearStrength;
}
