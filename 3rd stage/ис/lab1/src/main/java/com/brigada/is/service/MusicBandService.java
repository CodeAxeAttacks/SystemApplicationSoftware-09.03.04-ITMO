package com.brigada.is.service;

import com.brigada.is.domain.MusicBand;
import com.brigada.is.domain.Studio;
import com.brigada.is.dto.request.MusicBandRequestDTO;
import com.brigada.is.dto.response.MusicBandResponseDTO;
import com.brigada.is.dto.response.NominationResponseDTO;
import com.brigada.is.exception.NotFoundException;
import com.brigada.is.mapper.MusicBandMapper;
import com.brigada.is.mapper.NominationMapper;
import com.brigada.is.mapper.StudioMapper;
import com.brigada.is.repository.MusicBandRepository;
import com.brigada.is.repository.NominationRepository;
import com.brigada.is.repository.StudioRepository;
import com.brigada.is.security.entity.User;
import com.brigada.is.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicBandService {
    private final PermissionService permissionService;
    private final MusicBandRepository musicBandRepository;
    private final StudioRepository studioRepository;
    private final UserRepository userRepository;
    private final NominationRepository nominationRepository;

    public List<MusicBandResponseDTO> getAllMusicBands() {
        return musicBandRepository.findAllByOrderByIdDesc().stream().map(MusicBandMapper.INSTANCE::toResponseDTO).toList();
    }

    public MusicBandResponseDTO getMusicBandById(Long id) {
        return musicBandRepository.findById(id).map(MusicBandMapper.INSTANCE::toResponseDTO).orElseThrow(() ->
                new NotFoundException("Music band doesn't exist"));
    }

    public MusicBandResponseDTO getWithMaxEstablishmentDate() {
        return musicBandRepository.findTopByOrderByEstablishmentDateDesc().map(MusicBandMapper.INSTANCE::toResponseDTO).orElseThrow(() ->
                new NotFoundException("Music band doesn't exist"));
    }

    public Long countMusicBandByStudio(Long id) {
        Studio studio = studioRepository.findById(id).orElseThrow(() -> new NotFoundException("Studio was not found"));

        return musicBandRepository.countByStudio(studio);
    }

    public List<MusicBandResponseDTO> getAlbumsCountGreater(Long albumsCount) {
        return musicBandRepository.findAllByAlbumsCountGreaterThan(albumsCount).stream().map(MusicBandMapper.INSTANCE::toResponseDTO).toList();
    }

    public NominationResponseDTO nominate(Long id) {
        MusicBand musicBand = musicBandRepository.findById(id).orElseThrow(() -> new NotFoundException("Music band was not found"));

        return NominationMapper.INSTANCE.toResponseDTO(nominationRepository.save(NominationMapper.INSTANCE.toEntity(musicBand)));
    }

    public MusicBandResponseDTO createMusicBand(MusicBandRequestDTO musicBandRequestDTO, Long studioId, String username) {
        MusicBand entity = MusicBandMapper.INSTANCE.toEntity(musicBandRequestDTO);
        if (studioId != -1) {
            Studio studio = studioRepository.findById(studioId).orElseThrow(() -> new NotFoundException("Studio was not found"));
            entity.setStudio(studio);
        }

        User user = getUserByUsername(username);
        if (entity.getStudio() != null) entity.getStudio().setCreatedBy(user);
        entity.setCreatedBy(user);

        return MusicBandMapper.INSTANCE.toResponseDTO(musicBandRepository.save(entity));
    }

    public MusicBandResponseDTO updateMusicBand(MusicBandRequestDTO musicBandRequestDTO, Long id, Long studioId, String username) {
        MusicBand existingEntity = musicBandRepository.findById(id).orElseThrow(() -> new NotFoundException("Music band was not found"));

        User user = getUserByUsername(username);
        permissionService.checkPermission(user, existingEntity);

        MusicBandMapper.INSTANCE.updateMusicBand(musicBandRequestDTO, existingEntity);
        Studio oldStudio = existingEntity.getStudio();

        if (studioId != -1) {
            Studio studio = studioRepository.findById(studioId).orElseThrow(() -> new NotFoundException("Studio was not found"));
            existingEntity.setStudio(studio);
        } else {
            Studio newStudio = StudioMapper.INSTANCE.toEntity(musicBandRequestDTO.getStudio());
            newStudio.setCreatedBy(user);
            existingEntity.setStudio(newStudio);
        }

        MusicBand updated = musicBandRepository.save(existingEntity);
        cleanUpUnusedStudio(oldStudio);

        return MusicBandMapper.INSTANCE.toResponseDTO(updated);
    }

    public MusicBandResponseDTO addSingle(Long id) {
        MusicBand existingEntity = musicBandRepository.findById(id).orElseThrow(() -> new NotFoundException("Music band was not found"));
        existingEntity.setSinglesCount(existingEntity.getSinglesCount() + 1);
        return MusicBandMapper.INSTANCE.toResponseDTO(musicBandRepository.save(existingEntity));
    }

    public void deleteMusicBand(Long id, String username) {
        MusicBand musicBand = musicBandRepository.findById(id)
                .orElse(null);
        if (musicBand == null) return;

        User user = getUserByUsername(username);
        permissionService.checkPermission(user, musicBand);

        if (musicBand.getStudio() != null) {
            throw new IllegalStateException("Could not delete. There is related studio, delete it first");
        }

        musicBandRepository.deleteById(id);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private void cleanUpUnusedStudio(Studio studio) {
        if (studio != null && musicBandRepository.countByStudio(studio) == 0) {
            studioRepository.delete(studio);
        }
    }
}
