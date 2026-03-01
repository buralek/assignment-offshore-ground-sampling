package buralek.assignment.ground.domain.port;

import buralek.assignment.ground.domain.model.Sample;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SampleRepository {
    Sample save(Sample sample);

    Optional<Sample> findById(UUID id);

    List<Sample> findAll();

    List<Sample> findAllByLocationId(UUID locationId);

    List<Sample> findPage(UUID locationId, Instant afterTimestamp, UUID afterId, int limit);

    void deleteById(UUID id);
}
