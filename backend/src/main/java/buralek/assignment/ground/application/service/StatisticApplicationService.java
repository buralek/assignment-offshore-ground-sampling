package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.SampleStatisticResponse;

import java.util.UUID;

public interface StatisticApplicationService {
    SampleStatisticResponse getStatisticSample(UUID locationId);
}
