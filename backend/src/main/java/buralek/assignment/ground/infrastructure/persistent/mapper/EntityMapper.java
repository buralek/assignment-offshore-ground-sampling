package buralek.assignment.ground.infrastructure.persistent.mapper;

import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.infrastructure.persistent.entity.LocationEntity;
import buralek.assignment.ground.infrastructure.persistent.entity.SampleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
@Slf4j
@RequiredArgsConstructor
public class EntityMapper {
    public Sample toSampleModel(SampleEntity entity) {
        return buralek.assignment.ground.domain.model.Sample.builder()
                .id(entity.getId())
                .location(toLocationModel(entity.getLocation()))
                .timestamp(entity.getTimestamp())
                .zoneId(ZoneId.of(entity.getZoneId()))
                .unitWeight(entity.getUnitWeight())
                .waterContent(entity.getWaterContent())
                .shearStrength(entity.getShearStrength())
                .build();
    }

    public Location toLocationModel(LocationEntity entity) {
        return Location.builder()
                .id(entity.getId())
                .name(entity.getName())
                .zoneId(ZoneId.of(entity.getZoneId()))
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .build();
    }
}
