package com.delidronehubs.delidronehubs.controllers;

import com.delidronehubs.delidronehubs.classes.Merchandiser;
import com.delidronehubs.delidronehubs.services.MerchandiserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/merchandisers")
public class MerchandiserController {
    private final MerchandiserService merchandiserService;

    @Autowired
    public MerchandiserController(MerchandiserService merchandiserService) {
        this.merchandiserService = merchandiserService;
    }

    @GetMapping
    public ResponseEntity<List<Merchandiser>> getAllMerchandisers() {
        List<Merchandiser> merchandisers = merchandiserService.getAllMerchandisers();
        return new ResponseEntity<>(merchandisers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Merchandiser> getMerchandiserById(@PathVariable UUID id) {
        Merchandiser merchandiser = merchandiserService.getMerchandiserById(id);
        if (merchandiser != null) {
            return new ResponseEntity<>(merchandiser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Merchandiser> createMerchandiser(@RequestBody Merchandiser merchandiser) {
        Merchandiser createdMerchandiser = merchandiserService.createMerchandiser(merchandiser);
        return new ResponseEntity<>(createdMerchandiser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Merchandiser> updateMerchandiser(@PathVariable UUID id, @RequestBody Merchandiser merchandiser) {
        Merchandiser updatedMerchandiser = merchandiserService.updateMerchandiser(id, merchandiser);
        if (updatedMerchandiser != null) {
            return new ResponseEntity<>(updatedMerchandiser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMerchandiser(@PathVariable UUID id) {
        merchandiserService.deleteMerchandiser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
