package buralek.assignment.ground.domain.exception;

import java.util.UUID;

public class SampleNotFoundException extends RuntimeException {

    public SampleNotFoundException(UUID id) {
        super("Sample not found: " + id);
    }
}