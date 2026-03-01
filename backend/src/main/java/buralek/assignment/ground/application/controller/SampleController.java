package buralek.assignment.ground.application.controller;

import buralek.assignment.ground.application.dto.SamplePageResponse;
import buralek.assignment.ground.application.dto.SampleRequest;
import buralek.assignment.ground.application.dto.SampleResponse;
import buralek.assignment.ground.application.service.SampleApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
@Validated
@Tag(name = "Samples", description = "CRUD operations for samples")
public class SampleController {

    private final SampleApplicationService sampleApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new sample")
    public SampleResponse createSample(@Valid @RequestBody SampleRequest request) {
        return sampleApplicationService.createSample(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sample by ID")
    public SampleResponse getSample(@PathVariable UUID id) {
        return sampleApplicationService.getSampleById(id);
    }

    @GetMapping("/page")
    @Operation(summary = "Get samples page using cursor-based pagination (ordered by timestamp, id)")
    public SamplePageResponse getSamplesPage(
            @RequestParam(required = false) UUID locationId,
            @RequestParam(required = false) Instant afterTimestamp,
            @RequestParam(required = false) UUID afterId,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit) {
        return sampleApplicationService.getSamplesPage(locationId, afterTimestamp, afterId, limit);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sample")
    public SampleResponse updateSample(
            @PathVariable UUID id,
            @Valid @RequestBody SampleRequest request) {
        return sampleApplicationService.updateSample(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete sample")
    public void deleteSample(@PathVariable UUID id) {
        sampleApplicationService.deleteSample(id);
    }
}