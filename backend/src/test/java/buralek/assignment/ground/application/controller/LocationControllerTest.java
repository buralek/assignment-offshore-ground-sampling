package buralek.assignment.ground.application.controller;

import buralek.assignment.ground.application.dto.LocationResponse;
import buralek.assignment.ground.application.service.LocationApplicationService;
import buralek.assignment.ground.domain.exception.LocationNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationApplicationService locationApplicationService;

    @Test
    @DisplayName("WHEN calling getAllLocations endpoint, THEN list of all locations is returned")
    void getAllLocations1() throws Exception {
        UUID id = UUID.randomUUID();
        LocationResponse response = LocationResponse.builder()
                .id(id)
                .name("North Sea Platform A")
                .zoneId(ZoneId.of("Europe/Amsterdam"))
                .latitude(57.12345)
                .longitude(2.45678)
                .build();

        when(locationApplicationService.getAllLocations()).thenReturn(List.of(response));

        String expectedJson = """
        [
          {
            "id": "%s",
            "name": "North Sea Platform A",
            "zoneId": "Europe/Amsterdam",
            "latitude": 57.12345,
            "longitude": 2.45678
          }
        ]
        """.formatted(id);

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("WHEN calling getAllLocations endpoint and no location exist, THEN an empty list is returned")
    void getAllLocations2() throws Exception {
        when(locationApplicationService.getAllLocations())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("WHEN calling getLocationById endpoint, THEN a location with this id is returned")
    void getLocationById_returnsLocation() throws Exception {
        UUID id = UUID.randomUUID();

        LocationResponse response = LocationResponse.builder()
                .id(id)
                .name("North Sea Platform A")
                .zoneId(ZoneId.of("Europe/Amsterdam"))
                .latitude(57.12345)
                .longitude(2.45678)
                .build();

        when(locationApplicationService.getLocationById(id))
                .thenReturn(response);

        String expectedJson = """
        {
          "id": "%s",
          "name": "North Sea Platform A",
          "zoneId": "Europe/Amsterdam",
          "latitude": 57.12345,
          "longitude": 2.45678
        }
        """.formatted(id);

        mockMvc.perform(get("/api/v1/locations/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("WHEN calling getLocationById endpoint and a location is not found, THEN 500 error is returned")
    void getLocationById_whenLocationNotFound_returns5xx() throws Exception {
        UUID id = UUID.randomUUID();

        when(locationApplicationService.getLocationById(id))
                .thenThrow(new LocationNotFoundException(id));

        mockMvc.perform(get("/api/v1/locations/{id}", id))
                .andExpect(status().is5xxServerError());
    }
}