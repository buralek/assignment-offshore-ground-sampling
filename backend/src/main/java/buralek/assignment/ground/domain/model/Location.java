package buralek.assignment.ground.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.ZoneId;
import java.util.UUID;

@Value
@Builder
public class Location {
    UUID id;
    String name;
    ZoneId zoneId;
    double latitude;
    double longitude;
}
