package buralek.assignment.ground.infrastructure.persistent.adapter;

import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.infrastructure.persistent.entity.LocationEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(LocationPersistentRepository.class)
class LocationPersistentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LocationPersistentRepository locationPersistentRepository;

    private LocationEntity persistLocation(String name, String zoneId, double latitude, double longitude) {
        return entityManager.persistAndFlush(new LocationEntity(name, ZoneId.of(zoneId), latitude, longitude));
    }

    private Location expectedLocation(LocationEntity entity) {
        return Location.builder()
                .id(entity.getId())
                .name(entity.getName())
                .zoneId(ZoneId.of(entity.getZoneId()))
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling findAll, THEN all persisted locations are returned as domain models with correct fields")
    void findAll1() {
        LocationEntity entity = persistLocation("North Sea Platform A", "Europe/Amsterdam", 57.12345, 2.45678);

        List<Location> result = locationPersistentRepository.findAll();

        assertThat(result).containsExactly(expectedLocation(entity));
    }

    @Test
    @DisplayName("WHEN calling findAll and no locations are persisted, THEN an empty list is returned")
    void findAll2() {
        assertThat(locationPersistentRepository.findAll()).isEmpty();
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling findById and the location exists, THEN the location domain model with correct fields is returned")
    void findById1() {
        LocationEntity entity = persistLocation("Gulf of Mexico Platform B", "America/Chicago", 28.91234, -90.12345);

        Optional<Location> result = locationPersistentRepository.findById(entity.getId());

        assertThat(result).contains(expectedLocation(entity));
    }

    @Test
    @DisplayName("WHEN calling findById and the location does not exist, THEN an empty Optional is returned")
    void findById2() {
        assertThat(locationPersistentRepository.findById(UUID.randomUUID())).isEmpty();
    }
}