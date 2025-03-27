package com.brigada.is.security.service;

import com.brigada.is.dto.response.AdminApplicationResponseDTO;
import com.brigada.is.exception.NotFoundException;
import com.brigada.is.mapper.AdminApplicationMapper;
import com.brigada.is.mapper.UserMapper;
import com.brigada.is.security.entity.AdminApplication;
import com.brigada.is.security.repository.AdminApplicationRepository;
import com.brigada.is.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminApplicationService {
    private final AdminApplicationRepository adminApplicationRepository;
    private final UserRepository userRepository;

    public List<AdminApplicationResponseDTO> getAllApplications() {
        return adminApplicationRepository.findAll()
                .stream()
                .map(AdminApplicationMapper.INSTANCE::toAdminApplicationResponseDTO)
                .collect(Collectors.toList());
    }

    public void approveApplication(Long id) {
        AdminApplication application = adminApplicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Application doesn't exist"));
        userRepository.save(UserMapper.INSTANCE.toUser(application));
        adminApplicationRepository.delete(application);
    }

    public void rejectApplication(Long id) {
        AdminApplication application = adminApplicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Application doesn't exist"));
        adminApplicationRepository.delete(application);
    }
}
