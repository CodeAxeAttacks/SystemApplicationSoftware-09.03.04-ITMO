package com.brigada.is.controller;

import com.brigada.is.security.jwt.JwtUtils;
import com.brigada.is.service.ImportService;
import com.brigada.is.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final MinioService minioService;
    private final JwtUtils jwtUtils;

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        InputStreamResource resource = minioService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @PostMapping
    public ResponseEntity<String> importStudyGroups(@RequestParam("file") MultipartFile file) {
        String username = jwtUtils.getCurrentUser().getUsername();
        Long result = importService.importStudyGroups(file, username);
        return ResponseEntity.ok("Import successful. Imported objects: " + result);
    }
}
