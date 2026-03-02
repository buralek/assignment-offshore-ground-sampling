package buralek.assignment.ground.application.controller;

import buralek.assignment.ground.application.dto.SampleStatisticResponse;
import buralek.assignment.ground.application.service.StatisticApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "Statistics endpoints")
public class StatisticController {
    private final StatisticApplicationService statisticApplicationService;

    @GetMapping("/samples")
    @Operation(summary = "Get sample static filtered by location id")
    public SampleStatisticResponse getSampleStatistic(
            @RequestParam(name = "locationId", required = false) UUID locationId) {
        log.info("Fetching statistics locationId={}", locationId);
        return statisticApplicationService.getStatisticSample(locationId);
    }
}