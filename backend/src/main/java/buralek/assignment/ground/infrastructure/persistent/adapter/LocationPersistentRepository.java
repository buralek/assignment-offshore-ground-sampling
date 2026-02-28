package buralek.assignment.ground.infrastructure.persistent.adapter;

import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.port.LocationRepository;
import buralek.assignment.ground.infrastructure.persistent.entity.LocationEntity;
import buralek.assignment.ground.infrastructure.persistent.repository.LocationJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocationPersistentRepository implements LocationRepository {

    private final LocationJpaRepository locationJpaRepository;

    @Override
    public List<Location> findAll() {
        return locationJpaRepository.findAll().stream()
                .map(this::toModel)
                .toList();
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return locationJpaRepository.findById(id)
                .map(this::toModel);
    }

    private Location toModel(LocationEntity entity) {
        return Location.builder()
                .id(entity.getId())
                .name(entity.getName())
                .zoneId(TimeZone.getTimeZone(entity.getZoneId()).toZoneId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}
