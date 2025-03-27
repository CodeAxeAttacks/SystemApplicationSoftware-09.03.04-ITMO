package com.brigada.is.controller;

import com.brigada.is.dto.response.ImportHistoryResponseDTO;
import com.brigada.is.security.jwt.JwtUtils;
import com.brigada.is.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/import-history")
@RequiredArgsConstructor
public class ImportHistoryController {
    private final JwtUtils jwtUtils;
    private final ImportService importService;

    @GetMapping("/my")
    public ResponseEntity<List<ImportHistoryResponseDTO>> getUsersImportHistory() {
        String username = jwtUtils.getCurrentUser().getUsername();
        return ResponseEntity.ok(importService.getUsersImportHistory(username));
    }

    @GetMapping
    public ResponseEntity<List<ImportHistoryResponseDTO>> getAllImportHistory() {
        return ResponseEntity.ok(importService.getAllImportHistory());
    }
}
