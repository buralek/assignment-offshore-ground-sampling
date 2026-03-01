package buralek.assignment.ground.application.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class SampleCursor {
    Instant afterTimestamp;
    UUID afterId;
}