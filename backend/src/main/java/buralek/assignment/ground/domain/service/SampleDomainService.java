package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.exception.LocationNotFoundException;
import buralek.assignment.ground.domain.exception.SampleNotFoundException;
import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.port.LocationRepository;
import buralek.assignment.ground.domain.port.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SampleDomainService {

    private final SampleRepository sampleRepository;
    private final LocationRepository locationRepository;

    public List<Sample> findAll() {
        return sampleRepository.findAll();
    }

    public Sample findById(UUID id) {
        return sampleRepository.findById(id)
                .orElseThrow(() -> new SampleNotFoundException(id));
    }

    public Sample create(UUID locationId, Instant timestamp, double unitWeight, double waterContent, double shearStrength) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        return sampleRepository.save(Sample.builder()
                .location(location)
                .timestamp(timestamp)
                .zoneId(location.getZoneId())
                .unitWeight(unitWeight)
                .waterContent(waterContent)
                .shearStrength(shearStrength)
                .build());
    }

    public Sample update(UUID id, UUID locationId, Instant timestamp, double unitWeight, double waterContent, double shearStrength) {
        Sample existing = findById(id);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        return sampleRepository.save(Sample.builder()
                .id(existing.getId())
                .location(location)
                .timestamp(timestamp)
                .zoneId(location.getZoneId())
                .unitWeight(unitWeight)
                .waterContent(waterContent)
                .shearStrength(shearStrength)
                .build());
    }

    public void deleteById(UUID id) {
        sampleRepository.deleteById(id);
    }
}
