package buralek.assignment.ground.application.mapper;

import buralek.assignment.ground.application.dto.LocationResponse;
import buralek.assignment.ground.domain.model.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationMapper {
    public LocationResponse toLocationResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .zoneId(location.getZoneId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}
