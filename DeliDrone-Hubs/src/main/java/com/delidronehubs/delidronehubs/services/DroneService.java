package com.delidronehubs.delidronehubs.services;

import com.delidronehubs.delidronehubs.classes.Drone;
import com.delidronehubs.delidronehubs.repositories.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DroneService {
    private final DroneRepository droneRepository;

    @Autowired
    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    public Drone getDroneById(UUID id) {
        return droneRepository.findById(id).orElse(null);
    }

    public Drone createDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    public Drone updateDrone(UUID id, Drone drone) {
        if (droneRepository.existsById(id)) {
            drone.setId(id);
            return droneRepository.save(drone);
        }
        return null;
    }

    public void deleteDrone(UUID id) {
        droneRepository.deleteById(id);
    }
}
