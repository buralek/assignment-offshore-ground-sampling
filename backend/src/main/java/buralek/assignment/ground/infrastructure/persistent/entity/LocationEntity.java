package buralek.assignment.ground.infrastructure.persistent.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "location")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String zoneId;

    private double latitude;

    private double longitude;

    @OneToMany(
            mappedBy = "location",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SampleEntity> samples = new ArrayList<>();

    public LocationEntity(String name, ZoneId zoneId, double latitude, double longitude) {
        this.name = Objects.requireNonNull(name, "Name is required");
        this.zoneId = Objects.requireNonNull(zoneId, "ZoneId is required").getId();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public List<SampleEntity> getSamples() {
        return Collections.unmodifiableList(samples);
    }
}