package buralek.assignment.ground.application.service;

import buralek.assignment.ground.application.dto.LocationResponse;
import buralek.assignment.ground.application.mapper.LocationMapper;
import buralek.assignment.ground.domain.exception.LocationNotFoundException;
import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.service.LocationDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationApplicationServiceImplTest {

    @Mock
    private LocationDomainService locationDomainService;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationApplicationServiceImpl locationApplicationService;

    private static final UUID LOCATION_ID = UUID.fromString("d848e96d-9168-4b9e-8b88-2b18841d15f1");

    private Location buildLocation() {
        return Location.builder()
                .id(LOCATION_ID)
                .name("North Sea Platform A")
                .zoneId(ZoneId.of("Europe/Amsterdam"))
                .latitude(57.12345)
                .longitude(2.45678)
                .build();
    }

    private LocationResponse buildLocationResponse() {
        return LocationResponse.builder()
                .id(LOCATION_ID)
                .name("North Sea Platform A")
                .zoneId(ZoneId.of("Europe/Amsterdam"))
                .latitude(57.12345)
                .longitude(2.45678)
                .build();
    }

    @Test
    @DisplayName("WHEN calling getAllLocations, THEN each domain location is mapped and returned")
    void getAllLocations1() {
        Location location = buildLocation();
        LocationResponse response = buildLocationResponse();

        when(locationDomainService.findAll()).thenReturn(List.of(location));
        when(locationMapper.toLocationResponse(location)).thenReturn(response);

        List<LocationResponse> result = locationApplicationService.getAllLocations();

        assertThat(result).containsExactly(response);
        verify(locationMapper).toLocationResponse(location);
    }

    @Test
    @DisplayName("WHEN calling getAllLocations and no locations exist, THEN an empty list is returned")
    void getAllLocations2() {
        when(locationDomainService.findAll()).thenReturn(List.of());

        List<LocationResponse> result = locationApplicationService.getAllLocations();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("WHEN calling getLocationById, THEN the domain location is mapped and returned")
    void getLocationById1() {
        Location location = buildLocation();
        LocationResponse response = buildLocationResponse();

        when(locationDomainService.findById(LOCATION_ID)).thenReturn(location);
        when(locationMapper.toLocationResponse(location)).thenReturn(response);

        LocationResponse result = locationApplicationService.getLocationById(LOCATION_ID);

        assertThat(result).isEqualTo(response);
        verify(locationMapper).toLocationResponse(location);
    }

    @Test
    @DisplayName("WHEN calling getLocationById and the location does not exist, THEN LocationNotFoundException is propagated")
    void getLocationById2() {
        when(locationDomainService.findById(LOCATION_ID))
                .thenThrow(new LocationNotFoundException(LOCATION_ID));

        assertThatThrownBy(() -> locationApplicationService.getLocationById(LOCATION_ID))
                .isInstanceOf(LocationNotFoundException.class);
    }
}