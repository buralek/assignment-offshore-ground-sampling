package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.exception.LocationNotFoundException;
import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.port.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationDomainService {

    private final LocationRepository locationRepository;

    public List<Location> findAll() {
        List<Location> locations = locationRepository.findAll();
        log.info("Found {} locations", locations.size());
        return locations;
    }

    public Location findById(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
        log.info("Found location [{}]", location);
        return location;
    }
}