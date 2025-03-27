package com.brigada.is.controller;

import com.brigada.is.dto.response.NominationResponseDTO;
import com.brigada.is.service.NominationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nominations")
@RequiredArgsConstructor
public class NominationController {
    private final NominationService nominationService;

    @GetMapping
    public ResponseEntity<List<NominationResponseDTO>> getAllNominations() {
        return ResponseEntity.ok(nominationService.getAllNominations());
    }
}
