package buralek.assignment.ground.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import java.time.ZoneId;
import java.util.UUID;

@Value
@Builder
@Schema(description = "Location information")
public class LocationResponse {
    @Schema(example = "d848e96d-9168-4b9e-8b88-2b18841d15f1")
    UUID id;

    @Schema(example = "North Sea Platform A")
    String name;

    @Schema(example = "Europe/Amsterdam")
    ZoneId zoneId;

    @Schema(example = "57.12345")
    Double latitude;

    @Schema(example = "2.45678")
    Double longitude;
}
