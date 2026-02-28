package buralek.assignment.ground.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

@Value
@Builder
public class Sample {
    UUID id;
    Location location;
    Instant timestamp;
    ZoneId zoneId;

    // All stored in SI units
    double unitWeight;
    double waterContent;
    double shearStrength;

    // validation?
}
