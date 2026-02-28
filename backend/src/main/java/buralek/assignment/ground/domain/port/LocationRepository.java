package buralek.assignment.ground.domain.port;

import buralek.assignment.ground.domain.model.Location;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {
    List<Location> findAll();

    Optional<Location> findById(UUID id);

}
