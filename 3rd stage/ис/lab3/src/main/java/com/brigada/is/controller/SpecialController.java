package com.brigada.is.controller;

import com.brigada.is.dto.response.MusicBandResponseDTO;
import com.brigada.is.dto.response.NominationResponseDTO;
import com.brigada.is.service.MusicBandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/special")
@RequiredArgsConstructor
public class SpecialController {
    private final MusicBandService musicBandService;


    @GetMapping("/max-establishment-date")
    public ResponseEntity<MusicBandResponseDTO> getWithMaxEstablishmentDate() {
        return ResponseEntity.ok(musicBandService.getWithMaxEstablishmentDate());
    }

    @GetMapping("/count-by-studio")
    public ResponseEntity<Long> countByStudio(@RequestParam Long studioId) {
        return ResponseEntity.ok(musicBandService.countMusicBandByStudio(studioId));
    }

    @GetMapping("/albums-count-greater")
    public ResponseEntity<List<MusicBandResponseDTO>> getAlbumsCountGreater(@RequestParam Long albumsCount) {
        return ResponseEntity.ok(musicBandService.getAlbumsCountGreater(albumsCount));
    }

    @PostMapping("/nominate")
    public ResponseEntity<NominationResponseDTO> nominateBand(@RequestParam Long id) {
        return ResponseEntity.ok(musicBandService.nominate(id));
    }

    @PutMapping("/add-single")
    public ResponseEntity<MusicBandResponseDTO> addSingle(@RequestParam Long id) {
        return ResponseEntity.ok(musicBandService.addSingle(id));
    }
}
