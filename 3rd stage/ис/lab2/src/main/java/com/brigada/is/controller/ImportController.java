package com.brigada.is.controller;

import com.brigada.is.security.jwt.JwtUtils;
import com.brigada.is.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<String> importStudyGroups(@RequestParam("file") MultipartFile file) {
        String username = jwtUtils.getCurrentUser().getUsername();
        Long result = importService.importStudyGroups(file, username);
        return ResponseEntity.ok("Import successful. Imported objects: " + result);
    }
}
