package com.brigada.is.service;

import com.brigada.is.domain.Studio;
import com.brigada.is.dto.response.StudioResponseDTO;
import com.brigada.is.exception.NotFoundException;
import com.brigada.is.mapper.StudioMapper;
import com.brigada.is.repository.StudioRepository;
import com.brigada.is.security.entity.User;
import com.brigada.is.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final PermissionService permissionService;
    private final StudioRepository studioRepository;
    private final UserRepository userRepository;

    public List<StudioResponseDTO> getAllStudiosByUsername(String username) {
        User user = getUserByUsername(username);

        return studioRepository.findAllByCreatedBy(user).stream().map(StudioMapper.INSTANCE::toResponseDTO).toList();
    }

    public StudioResponseDTO getStudioById(Long id) {
        return studioRepository.findById(id).map(StudioMapper.INSTANCE::toResponseDTO).orElseThrow(() ->
                new NotFoundException("Studio doesn't exist"));
    }

    public void deleteStudio(Long id, String username) {
        Studio studio = studioRepository.findById(id)
                .orElse(null);
        if (studio == null) return;

        User user = getUserByUsername(username);
        permissionService.checkPermission(user, studio);

        studioRepository.deleteById(id);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

}
