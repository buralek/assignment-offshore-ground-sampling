package buralek.assignment.ground.infrastructure.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "sample")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String zoneId;

    // All stored in SI units
    private double unitWeight;
    private double waterContent;
    private double shearStrength;

    public SampleEntity(
            LocationEntity location,
            Instant timestamp,
            ZoneId zoneId,
            double unitWeight,
            double waterContent,
            double shearStrength
    ) {
        this.location = Objects.requireNonNull(location, "Location is required");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp is required");
        this.zoneId = Objects.requireNonNull(zoneId, "ZoneId is required").getId();
        this.unitWeight = unitWeight;
        this.waterContent = waterContent;
        this.shearStrength = shearStrength;
    }

    public SampleEntity(
            UUID id,
            LocationEntity location,
            Instant timestamp,
            ZoneId zoneId,
            double unitWeight,
            double waterContent,
            double shearStrength
    ) {
        this(location, timestamp, zoneId, unitWeight, waterContent, shearStrength);
        this.id = id;
    }
}
