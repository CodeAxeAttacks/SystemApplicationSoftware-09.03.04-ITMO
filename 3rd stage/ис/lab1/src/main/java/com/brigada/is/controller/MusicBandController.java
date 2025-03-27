package com.brigada.is.controller;

import com.brigada.is.dto.request.MusicBandRequestDTO;
import com.brigada.is.dto.response.MusicBandResponseDTO;
import com.brigada.is.security.jwt.JwtUtils;
import com.brigada.is.service.MusicBandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/music-bands")
@RequiredArgsConstructor
public class MusicBandController {
    private final MusicBandService musicBandService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<MusicBandResponseDTO>> getAllMusicBands() {
        return ResponseEntity.ok(musicBandService.getAllMusicBands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicBandResponseDTO> getMusicBandById(@PathVariable Long id) {
        return ResponseEntity.ok(musicBandService.getMusicBandById(id));
    }

    @Operation(description = "ACHTUNG если передаешь studioId, он будет пытаться найти существующий по id, иначе создаст новую")
    @PostMapping
    public ResponseEntity<MusicBandResponseDTO> createMusicBand(@RequestParam(required = false, defaultValue = "-1") Long studioId, @RequestBody MusicBandRequestDTO musicBandRequestDTO) {
        String username = jwtUtils.getCurrentUser().getUsername();

        MusicBandResponseDTO createdBand = musicBandService.createMusicBand(musicBandRequestDTO, studioId, username);
        return new ResponseEntity<>(createdBand, HttpStatus.CREATED);
    }

    @Operation(description = "ACHTUNG если передаешь studioId, он будет пытаться найти существующий по id, иначе создаст новую")
    @PutMapping("/{id}")
    public ResponseEntity<MusicBandResponseDTO> updateMusicBand(@RequestParam(required = false, defaultValue = "-1") Long studioId, @PathVariable Long id, @RequestBody MusicBandRequestDTO musicBandRequestDTO) {
        String username = jwtUtils.getCurrentUser().getUsername();

        return ResponseEntity.ok(musicBandService.updateMusicBand(musicBandRequestDTO, id, studioId, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MusicBandResponseDTO> deleteMusicBand(@PathVariable Long id) {
        String username = jwtUtils.getCurrentUser().getUsername();
        musicBandService.deleteMusicBand(id, username);
        return ResponseEntity.noContent().build();
    }
}
