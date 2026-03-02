package buralek.assignment.ground.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Jacksonized
@Schema(description = "Request payload for creating or updating a sample")
public class SampleRequest {
    @NotNull
    @Schema(description = "Location identifier",
            example = "550e8400-e29b-41d4-a716-446655440000")
    UUID locationId;

    @NotNull
    @Schema(description = "Sampling timestamp in UTC (ISO-8601)",
            example = "2026-07-01T08:15:30Z")
    Instant samplingTimestamp;

    @NotNull
    @Positive
    @Schema(description = "Sampling depth in metres",
            example = "5.5")
    Double depth;

    @NotNull
    @Positive
    @Schema(description = "Unit weight in kN/m³",
            example = "18.7")
    Double unitWeight;

    @NotNull
    @Positive
    @Schema(description = "Water content in percentage",
            example = "22.4")
    Double waterContent;

    @NotNull
    @Positive
    @Schema(description = "Shear strength in kPa",
            example = "35.8")
    Double shearStrength;

    @JsonCreator
    public SampleRequest(
            @JsonProperty("locationId") UUID locationId,
            @JsonProperty("samplingTimestamp") Instant samplingTimestamp,
            @JsonProperty("depth") Double depth,
            @JsonProperty("unitWeight") Double unitWeight,
            @JsonProperty("waterContent") Double waterContent,
            @JsonProperty("shearStrength") Double shearStrength
    ) {
        this.locationId = locationId;
        this.samplingTimestamp = samplingTimestamp;
        this.depth = depth;
        this.unitWeight = unitWeight;
        this.waterContent = waterContent;
        this.shearStrength = shearStrength;
    }
}
