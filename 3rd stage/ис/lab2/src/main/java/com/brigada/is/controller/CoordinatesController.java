package com.brigada.is.controller;

import com.brigada.is.dto.response.CoordinatesResponseDTO;
import com.brigada.is.service.CoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
@RequiredArgsConstructor
public class CoordinatesController {
    private final CoordinatesService coordinatesService;

    @GetMapping
    public ResponseEntity<List<CoordinatesResponseDTO>> getAllCoordinates() {
        return ResponseEntity.ok(coordinatesService.getAllCoordinates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoordinatesResponseDTO> getCoordinatesById(@PathVariable Long id) {
        return ResponseEntity.ok(coordinatesService.getCoordinatesById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinatesById(@PathVariable Long id) {
        coordinatesService.deleteCoordinatesById(id);
        return ResponseEntity.noContent().build();
    }
}
