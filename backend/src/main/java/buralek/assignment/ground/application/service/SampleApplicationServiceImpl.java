package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.SampleCursor;
import buralek.assignment.ground.application.dto.SamplePageResponse;
import buralek.assignment.ground.application.dto.SampleRequest;
import buralek.assignment.ground.application.dto.SampleResponse;
import buralek.assignment.ground.application.mapper.SampleMapper;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.model.SamplePage;
import buralek.assignment.ground.domain.service.SampleDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SampleApplicationServiceImpl implements SampleApplicationService {
    private final SampleDomainService sampleDomainService;
    private final SampleMapper sampleMapper;

    @Transactional(readOnly = true)
    public SampleResponse getSampleById(UUID id) {
        return sampleMapper.toSampleResponse(sampleDomainService.findById(id));
    }

    @Transactional
    public SampleResponse createSample(SampleRequest request) {
        Sample sample = sampleDomainService.create(
                request.getLocationId(),
                request.getSamplingTimestamp(),
                request.getUnitWeight(),
                request.getWaterContent(),
                request.getShearStrength()
        );
        return sampleMapper.toSampleResponse(sample);
    }

    @Transactional
    public SampleResponse updateSample(UUID id, SampleRequest request) {
        Sample sample = sampleDomainService.update(
                id,
                request.getLocationId(),
                request.getSamplingTimestamp(),
                request.getUnitWeight(),
                request.getWaterContent(),
                request.getShearStrength()
        );
        return sampleMapper.toSampleResponse(sample);
    }

    @Transactional(readOnly = true)
    public SamplePageResponse getSamplesPage(UUID locationId, Instant afterTimestamp, UUID afterId, int limit) {
        SamplePage page = sampleDomainService.findPage(locationId, afterTimestamp, afterId, limit);
        List<SampleResponse> data = page.getSamples().stream()
                .map(sampleMapper::toSampleResponse)
                .toList();
        SampleCursor nextCursor = page.isHasMore() ? buildNextCursor(page.getSamples()) : null;
        return SamplePageResponse.builder()
                .data(data)
                .hasMore(page.isHasMore())
                .nextCursor(nextCursor)
                .build();
    }

    private SampleCursor buildNextCursor(List<Sample> samples) {
        Sample last = samples.getLast();
        return SampleCursor.builder()
                .afterTimestamp(last.getTimestamp())
                .afterId(last.getId())
                .build();
    }

    @Transactional
    public void deleteSample(UUID id) {
        sampleDomainService.deleteById(id);
    }
}
