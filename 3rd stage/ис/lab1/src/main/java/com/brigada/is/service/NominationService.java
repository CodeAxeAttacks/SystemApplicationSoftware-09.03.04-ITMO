package com.brigada.is.service;

import com.brigada.is.dto.response.NominationResponseDTO;
import com.brigada.is.mapper.NominationMapper;
import com.brigada.is.repository.NominationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NominationService {
    private final NominationRepository nominationRepository;

    public List<NominationResponseDTO> getAllNominations() {
        return nominationRepository.findAll().stream().map(NominationMapper.INSTANCE::toResponseDTO).toList();
    }
}
