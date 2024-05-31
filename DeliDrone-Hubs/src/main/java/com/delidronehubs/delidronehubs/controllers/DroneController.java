package com.delidronehubs.delidronehubs.controllers;

import com.delidronehubs.delidronehubs.classes.Drone;
import com.delidronehubs.delidronehubs.services.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drones")
public class DroneController {
    private final DroneService droneService;

    @Autowired
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @GetMapping
    public ResponseEntity<List<Drone>> getAllDrones() {
        List<Drone> drones = droneService.getAllDrones();
        return new ResponseEntity<>(drones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drone> getDroneById(@PathVariable UUID id) {
        Drone drone = droneService.getDroneById(id);
        if (drone != null) {
            return new ResponseEntity<>(drone, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Drone> createDrone(@RequestBody Drone drone) {
        Drone createdDrone = droneService.createDrone(drone);
        return new ResponseEntity<>(createdDrone, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drone> updateDrone(@PathVariable UUID id, @RequestBody Drone drone) {
        Drone updatedDrone = droneService.updateDrone(id, drone);
        if (updatedDrone != null) {
            return new ResponseEntity<>(updatedDrone, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrone(@PathVariable UUID id) {
        droneService.deleteDrone(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
