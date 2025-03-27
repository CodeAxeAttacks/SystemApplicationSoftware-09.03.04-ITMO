package com.brigada.is.controller;

import com.brigada.is.dto.response.StudioResponseDTO;
import com.brigada.is.security.jwt.JwtUtils;
import com.brigada.is.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studios")
@RequiredArgsConstructor
public class StudioController {
    private final StudioService studioService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<StudioResponseDTO>> getAllStudiosByUser() {
        String username = jwtUtils.getCurrentUser().getUsername();

        return ResponseEntity.ok(studioService.getAllStudiosByUsername(username));

    }

    @GetMapping("/{id}")
    public ResponseEntity<StudioResponseDTO> getStudioById(@PathVariable Long id) {

        return ResponseEntity.ok(studioService.getStudioById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudioResponseDTO> deleteStudio(@PathVariable Long id) {
        String username = jwtUtils.getCurrentUser().getUsername();
        studioService.deleteStudio(id, username);
        return ResponseEntity.noContent().build();
    }
}
