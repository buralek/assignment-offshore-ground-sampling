package buralek.assignment.ground.infrastructure.persistent.repository;

import buralek.assignment.ground.infrastructure.persistent.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SampleJpaRepository extends JpaRepository<SampleEntity, UUID> {
    List<SampleEntity> findByLocationId(UUID locationId);
}
