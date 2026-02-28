package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.SampleRequest;
import buralek.assignment.ground.application.dto.SampleResponse;

import java.util.List;
import java.util.UUID;

public interface SampleApplicationService {

    List<SampleResponse> getAllSamples();

    SampleResponse getSampleById(UUID id);

    SampleResponse createSample(SampleRequest request);

    SampleResponse updateSample(UUID id, SampleRequest request);

    void deleteSample(UUID id);
}