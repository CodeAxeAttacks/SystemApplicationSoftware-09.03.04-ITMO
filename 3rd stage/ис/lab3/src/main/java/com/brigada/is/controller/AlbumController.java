package com.brigada.is.controller;

import com.brigada.is.dto.response.AlbumResponseDTO;
import com.brigada.is.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> getAllAlbums() {
        return ResponseEntity.ok(albumService.getAllAlbums());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbumById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbumById(@PathVariable Long id) {
        albumService.deleteAlbumById(id);
        return ResponseEntity.noContent().build();
    }
}
