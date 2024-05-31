package com.delidronehubs.delidronehubs.repositories;

import com.delidronehubs.delidronehubs.classes.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DroneRepository extends JpaRepository<Drone, UUID> {

}
