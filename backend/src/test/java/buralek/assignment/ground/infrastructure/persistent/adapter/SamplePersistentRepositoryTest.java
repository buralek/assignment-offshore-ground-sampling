package buralek.assignment.ground.infrastructure.persistent.adapter;

import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.infrastructure.persistent.entity.LocationEntity;
import buralek.assignment.ground.infrastructure.persistent.entity.SampleEntity;
import buralek.assignment.ground.infrastructure.persistent.mapper.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@Import({SamplePersistentRepository.class, EntityMapper.class})
class SamplePersistentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SamplePersistentRepository samplePersistentRepository;

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Amsterdam");
    private static final Instant DATE = Instant.parse("2026-07-01T08:15:30Z");

    private LocationEntity locationEntity;

    @BeforeEach
    void setUp() {
        locationEntity = entityManager.persistAndFlush(
                new LocationEntity("North Sea Platform A", ZONE_ID, 57.12345, 2.45678));
    }

    private SampleEntity persistSample() {
        entityManager.clear();
        LocationEntity managed = entityManager.find(LocationEntity.class, locationEntity.getId());
        SampleEntity sample = new SampleEntity(managed, DATE, ZONE_ID, 18.7, 22.4, 35.8);
        return entityManager.persistAndFlush(sample);
    }

    private Sample buildDomainSample(
            UUID sampleId,
            double unitWeight,
            double waterContent,
            double shearStrength) {
        Location location = Location.builder()
                .id(locationEntity.getId())
                .name(locationEntity.getName())
                .zoneId(ZONE_ID)
                .latitude(locationEntity.getLatitude())
                .longitude(locationEntity.getLongitude())
                .build();
        return Sample.builder()
                .id(sampleId)
                .location(location)
                .timestamp(DATE)
                .zoneId(ZONE_ID)
                .unitWeight(unitWeight)
                .waterContent(waterContent)
                .shearStrength(shearStrength)
                .build();
    }

    private Sample expectedSample(SampleEntity entity) {
        return Sample.builder()
                .id(entity.getId())
                .location(Location.builder()
                        .id(locationEntity.getId())
                        .name(locationEntity.getName())
                        .zoneId(ZONE_ID)
                        .latitude(locationEntity.getLatitude())
                        .longitude(locationEntity.getLongitude())
                        .build())
                .timestamp(entity.getTimestamp())
                .zoneId(ZoneId.of(entity.getZoneId()))
                .unitWeight(entity.getUnitWeight())
                .waterContent(entity.getWaterContent())
                .shearStrength(entity.getShearStrength())
                .build();
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling findAll, THEN all persisted samples are returned as domain models with correct fields")
    void findAll1() {
        SampleEntity persisted = persistSample();

        List<Sample> result = samplePersistentRepository.findAll();

        assertThat(result).containsExactly(expectedSample(persisted));
    }

    @Test
    @DisplayName("WHEN calling findAll and no samples are persisted, THEN an empty list is returned")
    void findAll2() {
        assertThat(samplePersistentRepository.findAll()).isEmpty();
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling findById and the sample exists, THEN the sample domain model with correct fields is returned")
    void findById1() {
        SampleEntity persisted = persistSample();

        Optional<Sample> result = samplePersistentRepository.findById(persisted.getId());

        assertThat(result).contains(expectedSample(persisted));
    }

    @Test
    @DisplayName("WHEN calling findById and the sample does not exist, THEN an empty Optional is returned")
    void findById2() {
        assertThat(samplePersistentRepository.findById(UUID.randomUUID())).isEmpty();
    }

    // ── save (create) ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling save with no id, THEN a new sample is persisted and returned with a generated id and correct fields")
    void save1() {
        Sample result = samplePersistentRepository.save(buildDomainSample(null, 18.7, 22.4, 35.8));
        entityManager.flush();
        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(expectedSample(entityManager.find(SampleEntity.class, result.getId())));
    }

    // ── save (update) ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling save with an existing id, THEN the existing sample is updated in the database")
    void save2() {
        SampleEntity persisted = persistSample();
        entityManager.clear();

        Sample updated = buildDomainSample(persisted.getId(), 25.0, 30.0, 50.0);

        samplePersistentRepository.save(updated);
        entityManager.flush();
        entityManager.clear();

        SampleEntity stored = entityManager.find(SampleEntity.class, persisted.getId());
        assertThat(stored).isNotNull();
        assertThat(stored.getUnitWeight()).isEqualTo(25.0);
        assertThat(stored.getWaterContent()).isEqualTo(30.0);
        assertThat(stored.getShearStrength()).isEqualTo(50.0);
    }

    // ── deleteById ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling deleteById, THEN the sample is removed from the database")
    void deleteById1() {
        SampleEntity persisted = persistSample();
        entityManager.clear();

        samplePersistentRepository.deleteById(persisted.getId());
        entityManager.flush();

        assertThat(entityManager.find(SampleEntity.class, persisted.getId())).isNull();
    }

    @Test
    @DisplayName("WHEN calling deleteById with a non-existent id, THEN no exception is thrown")
    void deleteById2() {
        assertThatNoException()
                .isThrownBy(() -> samplePersistentRepository.deleteById(UUID.randomUUID()));
    }

    // ── findPage ──────────────────────────────────────────────────────────────

    private SampleEntity persistSampleAt(Instant timestamp) {
        entityManager.clear();
        LocationEntity managed = entityManager.find(LocationEntity.class, locationEntity.getId());
        return entityManager.persistAndFlush(new SampleEntity(managed, timestamp, ZONE_ID, 10.0, 20.0, 30.0));
    }

    @Test
    @DisplayName("WHEN calling findPage with no cursor, THEN samples are returned in ascending timestamp order")
    void findPage1() {
        Instant earlier = Instant.ofEpochSecond(1_000_000);
        Instant later = Instant.ofEpochSecond(2_000_000);
        persistSampleAt(later);
        persistSampleAt(earlier);

        List<Sample> result = samplePersistentRepository.findPage(null, null, null, 10);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTimestamp()).isEqualTo(earlier);
        assertThat(result.get(1).getTimestamp()).isEqualTo(later);
    }

    @Test
    @DisplayName("WHEN calling findPage with a cursor, THEN only samples after the cursor are returned")
    void findPage2() {
        Instant t1 = Instant.ofEpochSecond(1_000_000);
        Instant t2 = Instant.ofEpochSecond(2_000_000);
        SampleEntity s1 = persistSampleAt(t1);
        persistSampleAt(t2);

        List<Sample> result = samplePersistentRepository.findPage(null, s1.getTimestamp(), s1.getId(), 10);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTimestamp()).isEqualTo(t2);
    }

    @Test
    @DisplayName("WHEN calling findPage with a limit smaller than total samples, THEN no more than limit samples are returned")
    void findPage3() {
        persistSampleAt(Instant.ofEpochSecond(1_000_000));
        persistSampleAt(Instant.ofEpochSecond(2_000_000));
        persistSampleAt(Instant.ofEpochSecond(3_000_000));

        List<Sample> result = samplePersistentRepository.findPage(null, null, null, 2);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("WHEN calling findPage with a locationId, THEN only samples from that location are returned")
    void findPage4() {
        LocationEntity otherLocation = entityManager.persistAndFlush(
                new LocationEntity("South Sea Platform B", ZONE_ID, 51.0, 3.0));

        persistSampleAt(Instant.ofEpochSecond(1_000_000));

        entityManager.clear();
        LocationEntity managedOther = entityManager.find(LocationEntity.class, otherLocation.getId());
        entityManager.persistAndFlush(new SampleEntity(managedOther, Instant.ofEpochSecond(2_000_000), ZONE_ID, 10.0, 20.0, 30.0));

        List<Sample> result = samplePersistentRepository.findPage(locationEntity.getId(), null, null, 10);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getLocation().getId()).isEqualTo(locationEntity.getId());
    }
}