package buralek.assignment.ground.infrastructure.persistent.repository;

import buralek.assignment.ground.infrastructure.persistent.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, UUID> {
}
