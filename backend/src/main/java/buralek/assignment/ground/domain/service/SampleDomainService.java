package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.exception.LocationNotFoundException;
import buralek.assignment.ground.domain.exception.SampleNotFoundException;
import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.model.SamplePage;
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
        List<Sample> samples = sampleRepository.findAll();
        log.info("Found [{}] samples", samples.size());
        return samples;
    }

    public Sample findById(UUID id) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new SampleNotFoundException(id));
        log.info("Found sample [{}]", sample);
        return sample;
    }

    public Sample create(UUID locationId, Instant timestamp, double depth, double unitWeight, double waterContent, double shearStrength) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        Sample saved = sampleRepository.save(Sample.builder()
                .location(location)
                .timestamp(timestamp)
                .zoneId(location.getZoneId())
                .depth(depth)
                .unitWeight(unitWeight)
                .waterContent(waterContent)
                .shearStrength(shearStrength)
                .build());
        log.info("Created sample [{}] for location [{}, {}]", saved.getId(), location.getId(), location.getName());
        return saved;
    }

    public Sample update(UUID id, UUID locationId, Instant timestamp, double depth, double unitWeight, double waterContent, double shearStrength) {
        Sample existing = findById(id);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> {
                    log.error("Location not found id={}", locationId);
                    return new LocationNotFoundException(locationId);
                });
        Sample saved = sampleRepository.save(Sample.builder()
                .id(existing.getId())
                .location(location)
                .timestamp(timestamp)
                .zoneId(location.getZoneId())
                .depth(depth)
                .unitWeight(unitWeight)
                .waterContent(waterContent)
                .shearStrength(shearStrength)
                .build());
        log.info("Updated sample [{}]", saved);
        return saved;
    }

    public SamplePage findPage(UUID locationId, Instant afterTimestamp, UUID afterId, int limit) {
        List<Sample> fetched = sampleRepository.findPage(locationId, afterTimestamp, afterId, limit + 1);
        boolean hasMore = fetched.size() > limit;
        List<Sample> samples = hasMore ? fetched.subList(0, limit) : fetched;
        log.info("Found page of [{}] samples, hasMore={}, locationId={}", samples.size(), hasMore, locationId);
        return SamplePage.builder()
                .samples(samples)
                .hasMore(hasMore)
                .build();
    }

    public void deleteById(UUID id) {
        sampleRepository.deleteById(id);
        log.info("Deleted sample with id [{}]", id);
    }
}