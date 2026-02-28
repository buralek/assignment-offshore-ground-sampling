package buralek.assignment.ground.domain.service;

import buralek.assignment.ground.domain.exception.LocationNotFoundException;
import buralek.assignment.ground.domain.model.Location;
import buralek.assignment.ground.domain.port.LocationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationDomainServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationDomainService locationDomainService;

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

    @Test
    @DisplayName("WHEN calling findAll, THEN all locations from the repository are returned")
    void findAll1() {
        Location location = buildLocation();
        when(locationRepository.findAll()).thenReturn(List.of(location));

        List<Location> result = locationDomainService.findAll();

        assertThat(result).containsExactly(location);
    }

    @Test
    @DisplayName("WHEN calling findAll and no locations exist, THEN an empty list is returned")
    void findAll2() {
        when(locationRepository.findAll()).thenReturn(List.of());

        List<Location> result = locationDomainService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("WHEN calling findById and the location exists, THEN the location is returned")
    void findById1() {
        Location location = buildLocation();
        when(locationRepository.findById(LOCATION_ID)).thenReturn(Optional.of(location));

        Location result = locationDomainService.findById(LOCATION_ID);

        assertThat(result).isEqualTo(location);
    }

    @Test
    @DisplayName("WHEN calling findById and the location does not exist, THEN LocationNotFoundException is thrown")
    void findById2() {
        when(locationRepository.findById(LOCATION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> locationDomainService.findById(LOCATION_ID))
                .isInstanceOf(LocationNotFoundException.class)
                .hasMessageContaining(LOCATION_ID.toString());
    }
}