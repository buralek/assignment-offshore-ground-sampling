package buralek.assignment.ground.infrastructure.persistent.repository;

import buralek.assignment.ground.infrastructure.persistent.entity.SampleEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface SampleJpaRepository extends JpaRepository<SampleEntity, UUID> {

    List<SampleEntity> findByLocationId(UUID locationId);

    @Query("SELECT s FROM sample s WHERE (:locationId IS NULL OR s.location.id = :locationId) ORDER BY s.timestamp ASC, s.id ASC")
    List<SampleEntity> findFirstPage(@Param("locationId") UUID locationId, Limit limit);

    @Query("""
            SELECT s FROM sample s
            WHERE (:locationId IS NULL OR s.location.id = :locationId)
              AND (s.timestamp > :afterTimestamp OR (s.timestamp = :afterTimestamp AND s.id > :afterId))
            ORDER BY s.timestamp ASC, s.id ASC
            """)
    List<SampleEntity> findNextPage(
            @Param("locationId") UUID locationId,
            @Param("afterTimestamp") Instant afterTimestamp,
            @Param("afterId") UUID afterId,
            Limit limit
    );
}
