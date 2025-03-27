package com.brigada.is.security.service;

import com.brigada.is.mapper.UserMapper;
import com.brigada.is.security.entity.Role;
import com.brigada.is.security.entity.User;
import com.brigada.is.security.repository.AdminApplicationRepository;
import com.brigada.is.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminApplicationRepository adminApplicationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByPassword(String encodedPassword) {
        return userRepository.existsByPassword(encodedPassword);
    }

    public boolean addUser(User user) {
        if (user.getRoles().contains(Role.ADMIN) && userRepository.existsUserWithRole(Role.ADMIN)) {
            adminApplicationRepository.save(UserMapper.INSTANCE.toAdminApplication(user));
            return false;
        } else {
            userRepository.save(user);
            return true;
        }
    }
}
