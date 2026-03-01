package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.SamplePageResponse;
import buralek.assignment.ground.application.dto.SampleRequest;
import buralek.assignment.ground.application.dto.SampleResponse;
import java.time.Instant;
import java.util.UUID;

public interface SampleApplicationService {

    SampleResponse getSampleById(UUID id);

    SampleResponse createSample(SampleRequest request);

    SampleResponse updateSample(UUID id, SampleRequest request);

    SamplePageResponse getSamplesPage(UUID locationId, Instant afterTimestamp, UUID afterId, int limit);

    void deleteSample(UUID id);
}