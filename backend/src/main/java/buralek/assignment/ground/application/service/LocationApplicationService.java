package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.LocationResponse;

import java.util.List;
import java.util.UUID;

public interface LocationApplicationService {

    List<LocationResponse> getAllLocations();

    LocationResponse getLocationById(UUID id);
}