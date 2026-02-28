package buralek.assignment.ground.application.controller;

import buralek.assignment.ground.application.dto.LocationResponse;
import buralek.assignment.ground.application.service.LocationApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "Locations", description = "Read-only access API to locations")
public class LocationController {

    private final LocationApplicationService locationApplicationService;

    @GetMapping
    @Operation(summary = "Get all locations")
    public List<LocationResponse> getAllLocations() {
        return locationApplicationService.getAllLocations();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a location by id")
    public LocationResponse getLocation(@PathVariable UUID id) {
        return locationApplicationService.getLocationById(id);
    }
}
