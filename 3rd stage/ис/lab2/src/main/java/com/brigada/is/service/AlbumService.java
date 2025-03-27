package com.brigada.is.service;

import com.brigada.is.domain.Album;
import com.brigada.is.dto.response.AlbumResponseDTO;
import com.brigada.is.exception.NotFoundException;
import com.brigada.is.mapper.AlbumMapper;
import com.brigada.is.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumResponseDTO getAlbumById(Long id) {
        Optional<Album> optional = albumRepository.findById(id);
        if (optional.isEmpty()) throw new NotFoundException("Album doesn't exist");
        return AlbumMapper.INSTANCE.toResponseDTO(optional.get());
    }

    public List<AlbumResponseDTO> getAllAlbums() {
        return albumRepository.findAll().stream()
                .map(AlbumMapper.INSTANCE::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteAlbumById(Long id) {
        albumRepository.deleteById(id);
    }
}
