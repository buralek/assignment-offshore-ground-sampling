package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.SamplePageResponse;
import buralek.assignment.ground.application.dto.SampleRequest;
import buralek.assignment.ground.application.dto.SampleResponse;
import buralek.assignment.ground.application.mapper.SampleMapper;
import buralek.assignment.ground.domain.exception.SampleNotFoundException;
import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.model.SamplePage;
import buralek.assignment.ground.domain.service.SampleDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SampleApplicationServiceImplTest {

    @Mock
    private SampleDomainService sampleDomainService;

    @Mock
    private SampleMapper sampleMapper;

    @InjectMocks
    private SampleApplicationServiceImpl sampleApplicationService;

    private static final UUID SAMPLE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID LOCATION_ID = UUID.fromString("661f9511-f3a2-52e5-b827-557766551111");
    private static final Instant SAMPLING_INSTANT = Instant.parse("2026-07-01T08:15:30Z");
    private static final OffsetDateTime SAMPLING_DATE = OffsetDateTime.parse("2026-07-01T10:15:30+02:00");

    private Sample buildSample() {
        return Sample.builder()
                .id(SAMPLE_ID)
                .location(Location.builder()
                        .id(LOCATION_ID)
                        .name("North Sea Platform A")
                        .zoneId(ZoneId.of("Europe/Amsterdam"))
                        .latitude(57.12345)
                        .longitude(2.45678)
                        .build())
                .timestamp(SAMPLING_INSTANT)
                .zoneId(ZoneId.of("Europe/Amsterdam"))
                .unitWeight(18.7)
                .waterContent(22.4)
                .shearStrength(35.8)
                .build();
    }

    private SampleResponse buildSampleResponse() {
        return SampleResponse.builder()
                .id(SAMPLE_ID)
                .locationId(LOCATION_ID)
                .timestampWithTimeZone(SAMPLING_DATE)
                .unitWeight(18.7)
                .waterContent(22.4)
                .shearStrength(35.8)
                .build();
    }

    private SampleRequest buildSampleRequest() {
        return SampleRequest.builder()
                .locationId(LOCATION_ID)
                .samplingTimestamp(SAMPLING_INSTANT)
                .unitWeight(18.7)
                .waterContent(22.4)
                .shearStrength(35.8)
                .build();
    }

    @Test
    @DisplayName("WHEN calling getSampleById, THEN the domain sample is mapped and returned")
    void getSampleById1() {
        Sample sample = buildSample();
        SampleResponse response = buildSampleResponse();

        when(sampleDomainService.findById(SAMPLE_ID)).thenReturn(sample);
        when(sampleMapper.toSampleResponse(sample)).thenReturn(response);

        SampleResponse result = sampleApplicationService.getSampleById(SAMPLE_ID);

        assertThat(result).isEqualTo(response);
        verify(sampleMapper).toSampleResponse(sample);
    }

    @Test
    @DisplayName("WHEN calling getSampleById and the sample does not exist, THEN SampleNotFoundException is propagated")
    void getSampleById2() {
        when(sampleDomainService.findById(SAMPLE_ID))
                .thenThrow(new SampleNotFoundException(SAMPLE_ID));

        assertThatThrownBy(() -> sampleApplicationService.getSampleById(SAMPLE_ID))
                .isInstanceOf(SampleNotFoundException.class);
    }

    @Test
    @DisplayName("WHEN calling createSample, THEN domain service is called with args extracted from the request and the result is mapped and returned")
    void createSample1() {
        SampleRequest request = buildSampleRequest();
        Sample sample = buildSample();
        SampleResponse response = buildSampleResponse();

        when(sampleDomainService.create(LOCATION_ID, SAMPLING_INSTANT, 18.7, 22.4, 35.8)).thenReturn(sample);
        when(sampleMapper.toSampleResponse(sample)).thenReturn(response);

        SampleResponse result = sampleApplicationService.createSample(request);

        assertThat(result).isEqualTo(response);
        verify(sampleDomainService).create(LOCATION_ID, SAMPLING_INSTANT, 18.7, 22.4, 35.8);
    }

    @Test
    @DisplayName("WHEN calling updateSample, THEN domain service is called with the id and args extracted from the request and the result is mapped and returned")
    void updateSample1() {
        SampleRequest request = buildSampleRequest();
        Sample sample = buildSample();
        SampleResponse response = buildSampleResponse();

        when(sampleDomainService.update(SAMPLE_ID, LOCATION_ID, SAMPLING_INSTANT, 18.7, 22.4, 35.8)).thenReturn(sample);
        when(sampleMapper.toSampleResponse(sample)).thenReturn(response);

        SampleResponse result = sampleApplicationService.updateSample(SAMPLE_ID, request);

        assertThat(result).isEqualTo(response);
        verify(sampleDomainService).update(SAMPLE_ID, LOCATION_ID, SAMPLING_INSTANT, 18.7, 22.4, 35.8);
    }

    @Test
    @DisplayName("WHEN calling deleteSample, THEN domain service deleteById is called with the given id")
    void deleteSample1() {
        sampleApplicationService.deleteSample(SAMPLE_ID);

        verify(sampleDomainService).deleteById(SAMPLE_ID);
    }

    // ── getSamplesPage ────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling getSamplesPage with hasMore=false, THEN samples are mapped and nextCursor is null")
    void getSamplesPage1() {
        Sample sample = buildSample();
        SampleResponse response = buildSampleResponse();
        SamplePage page = SamplePage.builder().samples(List.of(sample)).hasMore(false).build();

        when(sampleDomainService.findPage(null, null, null, 20)).thenReturn(page);
        when(sampleMapper.toSampleResponse(sample)).thenReturn(response);

        SamplePageResponse result = sampleApplicationService.getSamplesPage(null, null, null, 20);

        assertThat(result.getData()).containsExactly(response);
        assertThat(result.isHasMore()).isFalse();
        assertThat(result.getNextCursor()).isNull();
    }

    @Test
    @DisplayName("WHEN calling getSamplesPage with hasMore=true, THEN nextCursor is built from the last sample's timestamp and id")
    void getSamplesPage2() {
        Sample sample = buildSample();
        SampleResponse response = buildSampleResponse();
        SamplePage page = SamplePage.builder().samples(List.of(sample)).hasMore(true).build();

        when(sampleDomainService.findPage(null, null, null, 20)).thenReturn(page);
        when(sampleMapper.toSampleResponse(sample)).thenReturn(response);

        SamplePageResponse result = sampleApplicationService.getSamplesPage(null, null, null, 20);

        assertThat(result.isHasMore()).isTrue();
        assertThat(result.getNextCursor()).isNotNull();
        assertThat(result.getNextCursor().getAfterTimestamp()).isEqualTo(SAMPLING_INSTANT);
        assertThat(result.getNextCursor().getAfterId()).isEqualTo(SAMPLE_ID);
    }
}