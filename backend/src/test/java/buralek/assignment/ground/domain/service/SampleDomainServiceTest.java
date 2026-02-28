package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.exception.LocationNotFoundException;
import buralek.assignment.ground.domain.exception.SampleNotFoundException;
import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.model.Sample;
import buralek.assignment.ground.domain.port.LocationRepository;
import buralek.assignment.ground.domain.port.SampleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SampleDomainServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private SampleDomainService sampleDomainService;

    private static final UUID SAMPLE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID LOCATION_ID = UUID.fromString("661f9511-f3a2-52e5-b827-557766551111");
    private static final Instant DATE = Instant.parse("2026-07-01T08:15:30Z");
    private static final ZoneId ZONE_ID = ZoneId.of("Europe/Amsterdam");

    private Location buildLocation() {
        return Location.builder()
                .id(LOCATION_ID)
                .name("North Sea Platform A")
                .zoneId(ZONE_ID)
                .latitude(57.12345)
                .longitude(2.45678)
                .build();
    }

    private Sample buildSample() {
        return Sample.builder()
                .id(SAMPLE_ID)
                .location(buildLocation())
                .timestamp(DATE)
                .zoneId(ZONE_ID)
                .unitWeight(18.7)
                .waterContent(22.4)
                .shearStrength(35.8)
                .build();
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling findAll, THEN all samples from the repository are returned")
    void findAll1() {
        Sample sample = buildSample();
        when(sampleRepository.findAll()).thenReturn(List.of(sample));

        List<Sample> result = sampleDomainService.findAll();

        assertThat(result).containsExactly(sample);
    }

    @Test
    @DisplayName("WHEN calling findAll and no samples exist, THEN an empty list is returned")
    void findAll2() {
        when(sampleRepository.findAll()).thenReturn(List.of());

        List<Sample> result = sampleDomainService.findAll();

        assertThat(result).isEmpty();
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling findById and the sample exists, THEN the sample is returned")
    void findById1() {
        Sample sample = buildSample();
        when(sampleRepository.findById(SAMPLE_ID)).thenReturn(Optional.of(sample));

        Sample result = sampleDomainService.findById(SAMPLE_ID);

        assertThat(result).isEqualTo(sample);
    }

    @Test
    @DisplayName("WHEN calling findById and the sample does not exist, THEN SampleNotFoundException is thrown")
    void findById2() {
        when(sampleRepository.findById(SAMPLE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sampleDomainService.findById(SAMPLE_ID))
                .isInstanceOf(SampleNotFoundException.class)
                .hasMessageContaining(SAMPLE_ID.toString());
    }

    // ── create ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling create, THEN a sample is saved with correct fields and the timezone is snapshotted from the location")
    void create1() {
        Location location = buildLocation();
        Sample savedSample = buildSample();

        when(locationRepository.findById(LOCATION_ID)).thenReturn(Optional.of(location));
        when(sampleRepository.save(any())).thenReturn(savedSample);

        sampleDomainService.create(LOCATION_ID, DATE, 18.7, 22.4, 35.8);

        ArgumentCaptor<Sample> captor = ArgumentCaptor.forClass(Sample.class);
        verify(sampleRepository).save(captor.capture());

        Sample captured = captor.getValue();
        assertThat(captured.getId()).isNull();
        assertThat(captured.getLocation()).isEqualTo(location);
        assertThat(captured.getTimestamp()).isEqualTo(DATE);
        assertThat(captured.getZoneId()).isEqualTo(ZONE_ID);
        assertThat(captured.getUnitWeight()).isEqualTo(18.7);
        assertThat(captured.getWaterContent()).isEqualTo(22.4);
        assertThat(captured.getShearStrength()).isEqualTo(35.8);
    }

    @Test
    @DisplayName("WHEN calling create and the location does not exist, THEN LocationNotFoundException is thrown and nothing is saved")
    void create2() {
        when(locationRepository.findById(LOCATION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sampleDomainService.create(LOCATION_ID, DATE, 18.7, 22.4, 35.8))
                .isInstanceOf(LocationNotFoundException.class)
                .hasMessageContaining(LOCATION_ID.toString());

        verify(sampleRepository, never()).save(any());
    }

    // ── update ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling update, THEN the existing id is preserved, fields are updated, and the timezone is re-snapshotted from the location")
    void update1() {
        Sample existing = buildSample();
        Location location = buildLocation();
        Sample savedSample = buildSample();

        when(sampleRepository.findById(SAMPLE_ID)).thenReturn(Optional.of(existing));
        when(locationRepository.findById(LOCATION_ID)).thenReturn(Optional.of(location));
        when(sampleRepository.save(any())).thenReturn(savedSample);

        sampleDomainService.update(SAMPLE_ID, LOCATION_ID, DATE, 18.7, 22.4, 35.8);

        ArgumentCaptor<Sample> captor = ArgumentCaptor.forClass(Sample.class);
        verify(sampleRepository).save(captor.capture());

        Sample captured = captor.getValue();
        assertThat(captured.getId()).isEqualTo(SAMPLE_ID);
        assertThat(captured.getLocation()).isEqualTo(location);
        assertThat(captured.getTimestamp()).isEqualTo(DATE);
        assertThat(captured.getZoneId()).isEqualTo(ZONE_ID);
        assertThat(captured.getUnitWeight()).isEqualTo(18.7);
        assertThat(captured.getWaterContent()).isEqualTo(22.4);
        assertThat(captured.getShearStrength()).isEqualTo(35.8);
    }

    @Test
    @DisplayName("WHEN calling update and the sample does not exist, THEN SampleNotFoundException is thrown and nothing is saved")
    void update2() {
        when(sampleRepository.findById(SAMPLE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sampleDomainService.update(SAMPLE_ID, LOCATION_ID, DATE, 18.7, 22.4, 35.8))
                .isInstanceOf(SampleNotFoundException.class)
                .hasMessageContaining(SAMPLE_ID.toString());

        verify(sampleRepository, never()).save(any());
    }

    @Test
    @DisplayName("WHEN calling update and the location does not exist, THEN LocationNotFoundException is thrown and nothing is saved")
    void update3() {
        when(sampleRepository.findById(SAMPLE_ID)).thenReturn(Optional.of(buildSample()));
        when(locationRepository.findById(LOCATION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sampleDomainService.update(SAMPLE_ID, LOCATION_ID, DATE, 18.7, 22.4, 35.8))
                .isInstanceOf(LocationNotFoundException.class)
                .hasMessageContaining(LOCATION_ID.toString());

        verify(sampleRepository, never()).save(any());
    }

    // ── deleteById ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("WHEN calling deleteById and the sample exists, THEN the repository deleteById is called")
    void deleteById1() {
        sampleDomainService.deleteById(SAMPLE_ID);

        verify(sampleRepository).deleteById(SAMPLE_ID);
    }
}