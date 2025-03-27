package com.brigada.is.service;

import com.brigada.is.domain.ImportHistory;
import com.brigada.is.domain.ImportStatus;
import com.brigada.is.dto.request.MusicBandRequestDTO;
import com.brigada.is.dto.response.ImportHistoryResponseDTO;
import com.brigada.is.exception.ImportException;
import com.brigada.is.exception.NotFoundException;
import com.brigada.is.mapper.ImportHistoryMapper;
import com.brigada.is.repository.ImportHistoryRepository;
import com.brigada.is.security.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {
    private final ObjectMapper objectMapper;
    private final ImportHistoryRepository importHistoryRepository;
    private final MusicBandService musicBandService;
    private final UserRepository userRepository;

    public List<ImportHistoryResponseDTO> getAllImportHistory() {
        return importHistoryRepository.findAll().stream().map(ImportHistoryMapper.INSTANCE::toResponseDTO).toList();
    }

    public List<ImportHistoryResponseDTO> getUsersImportHistory(String username) {
        return importHistoryRepository.findAllByUser_Username(username).stream().map(ImportHistoryMapper.INSTANCE::toResponseDTO).toList();
    }

    @Transactional
    public Long importStudyGroups(MultipartFile file, String username) {
        List<MusicBandRequestDTO> musicBands;
        ImportHistory history = new ImportHistory();
        history.setUser(userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found")));
        try {
            musicBands = objectMapper.readValue(file.getInputStream(), new TypeReference<List<MusicBandRequestDTO>>() {
            });
        } catch (IOException e) {
            history.setStatus(ImportStatus.FAILED);
            importHistoryRepository.save(history);
            throw new ImportException("Error parsing the file", e);
        }

        try {
            for (MusicBandRequestDTO mb : musicBands) {
                musicBandService.createMusicBand(mb, -1L, username);
            }
            long importedCount = musicBands.size();
            history.setStatus(ImportStatus.SUCCESS);
            history.setObjectsCount(importedCount);
            importHistoryRepository.save(history);
            return importedCount;
        } catch (Exception e) {
            history.setStatus(ImportStatus.FAILED);
            importHistoryRepository.save(history);
            throw new ImportException("Import failed: " + e.getMessage());
        }
    }
}
