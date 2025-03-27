package com.brigada.is.controller;

import com.brigada.is.dto.response.AdminApplicationResponseDTO;
import com.brigada.is.security.service.AdminApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/applications")
@RequiredArgsConstructor
public class AdminApplicationController {
    private final AdminApplicationService applicationService;

    @GetMapping()
    public ResponseEntity<List<AdminApplicationResponseDTO>> getAllApplications() {
        return new ResponseEntity<>(applicationService.getAllApplications(), HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveApplication(@PathVariable Long id) {
        applicationService.approveApplication(id);
        return new ResponseEntity<>("Application approved", HttpStatus.OK);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable Long id) {
        applicationService.rejectApplication(id);
        return new ResponseEntity<>("Application rejected", HttpStatus.OK);
    }
}
