package buralek.assignment.ground.infrastructure.persistent.adapter;

import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.port.SampleRepository;
import buralek.assignment.ground.infrastructure.persistent.entity.LocationEntity;
import buralek.assignment.ground.infrastructure.persistent.entity.SampleEntity;
import buralek.assignment.ground.infrastructure.persistent.mapper.EntityMapper;
import buralek.assignment.ground.infrastructure.persistent.repository.LocationJpaRepository;
import buralek.assignment.ground.infrastructure.persistent.repository.SampleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SamplePersistentRepository implements SampleRepository {
    private final SampleJpaRepository sampleJpaRepository;
    private final LocationJpaRepository locationJpaRepository;
    private final EntityMapper entityMapper;

    @Override
    public Sample save(Sample sample) {
        LocationEntity locationEntity = locationJpaRepository.findById(sample.getLocation().getId())
                .orElseThrow();

        SampleEntity sampleEntity = sample.getId() != null
                ? new SampleEntity(sample.getId(), locationEntity, sample.getTimestamp(), sample.getZoneId(),
                        sample.getUnitWeight(), sample.getWaterContent(), sample.getShearStrength())
                : new SampleEntity(locationEntity, sample.getTimestamp(), sample.getZoneId(),
                        sample.getUnitWeight(), sample.getWaterContent(), sample.getShearStrength());

        return entityMapper.toSampleModel(sampleJpaRepository.save(sampleEntity));
    }

    @Override
    public Optional<Sample> findById(UUID id) {
        return sampleJpaRepository.findById(id).map(entityMapper::toSampleModel);
    }

    @Override
    public List<Sample> findAll() {
        return sampleJpaRepository.findAll().stream()
                .map(entityMapper::toSampleModel)
                .toList();
    }

    @Override
    public List<Sample> findAllByLocationId(UUID locationId) {
        return sampleJpaRepository.findByLocationId(locationId).stream()
                .map(entityMapper::toSampleModel)
                .toList();
    }

    @Override
    public List<Sample> findPage(UUID locationId, Instant afterTimestamp, UUID afterId, int limit) {
        List<SampleEntity> entities = afterTimestamp == null
                ? sampleJpaRepository.findFirstPage(locationId, Limit.of(limit))
                : sampleJpaRepository.findNextPage(locationId, afterTimestamp, afterId, Limit.of(limit));
        return entities.stream().map(entityMapper::toSampleModel).toList();
    }

    @Override
    public void deleteById(UUID id) {
        sampleJpaRepository.deleteById(id);
    }
}
