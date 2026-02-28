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
        return locationRepository.findAll();
    }

    public Location findById(UUID id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
    }
}
