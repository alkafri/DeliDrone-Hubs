package com.delidronehubs.delidronehubs.controllers;

import com.delidronehubs.delidronehubs.classes.Hub;
import com.delidronehubs.delidronehubs.services.HubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hubs")
public class HubController {
    private final HubService hubService;

    @Autowired
    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    @GetMapping
    public ResponseEntity<List<Hub>> getAllHubs() {
        List<Hub> hubs = hubService.getAllHubs();
        return new ResponseEntity<>(hubs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hub> getHubById(@PathVariable UUID id) {
        Hub hub = hubService.getHubById(id);
        if (hub != null) {
            return new ResponseEntity<>(hub, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Hub> createHub(@RequestBody Hub hub) {
        Hub createdHub = hubService.createHub(hub);
        return new ResponseEntity<>(createdHub, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hub> updateHub(@PathVariable UUID id, @RequestBody Hub hub) {
        Hub updatedHub = hubService.updateHub(id, hub);
        if (updatedHub != null) {
            return new ResponseEntity<>(updatedHub, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHub(@PathVariable UUID id) {
        hubService.deleteHub(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
