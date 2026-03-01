package buralek.assignment.ground.application.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenResponse {
    String token;
    long expiresIn;
}