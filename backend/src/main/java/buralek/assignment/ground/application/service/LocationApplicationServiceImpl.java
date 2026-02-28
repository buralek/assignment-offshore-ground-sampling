package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.LocationResponse;
import buralek.assignment.ground.application.mapper.LocationMapper;
import buralek.assignment.ground.domain.service.LocationDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationApplicationServiceImpl implements LocationApplicationService {
    private final LocationDomainService locationDomainService;
    private final LocationMapper locationMapper;

    @Transactional(readOnly = true)
    public List<LocationResponse> getAllLocations() {
        return locationDomainService.findAll().stream()
                .map(locationMapper::toLocationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationResponse getLocationById(UUID id) {
        return locationMapper.toLocationResponse(locationDomainService.findById(id));
    }

}