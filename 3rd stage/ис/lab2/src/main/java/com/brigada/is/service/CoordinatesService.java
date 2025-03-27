package com.brigada.is.service;

import com.brigada.is.domain.Coordinates;
import com.brigada.is.dto.response.CoordinatesResponseDTO;
import com.brigada.is.exception.NotFoundException;
import com.brigada.is.mapper.CoordinatesMapper;
import com.brigada.is.repository.CoordinatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoordinatesService {
    private final CoordinatesRepository coordinatesRepository;

    public CoordinatesResponseDTO getCoordinatesById(Long id) {
        Optional<Coordinates> optional = coordinatesRepository.findById(id);
        if (optional.isEmpty()) throw new NotFoundException("Coordinates don't exist");
        return CoordinatesMapper.INSTANCE.toResponseDTO(optional.get());
    }

    public List<CoordinatesResponseDTO> getAllCoordinates() {
        return coordinatesRepository.findAll().stream()
                .map(CoordinatesMapper.INSTANCE::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteCoordinatesById(Long id) {
        coordinatesRepository.deleteById(id);
    }
}
