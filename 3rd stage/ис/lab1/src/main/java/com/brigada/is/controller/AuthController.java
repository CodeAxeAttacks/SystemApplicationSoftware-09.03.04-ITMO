package com.brigada.is.controller;

import com.brigada.is.dto.request.LoginRequest;
import com.brigada.is.dto.request.RegisterRequest;
import com.brigada.is.dto.response.JwtResponse;
import com.brigada.is.exception.PasswordAlreadyExistsException;
import com.brigada.is.exception.UsernameAlreadyExistsException;
import com.brigada.is.security.entity.Role;
import com.brigada.is.security.entity.User;
import com.brigada.is.security.jwt.JwtUtils;
import com.brigada.is.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(generateJwtResponse(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
        String encodedPassword = encoder.encode(request.getPassword());
        if (userService.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (userService.existsByPassword(encodedPassword)) {
            throw new PasswordAlreadyExistsException("Password already exists");
        }
        User user = new User(request.getUsername(), encodedPassword);
        Set<String> rolesString = request.getRoles();
        Set<Role> roles = rolesString.stream().map(Role::valueOf).collect(Collectors.toSet());
        user.setRoles(roles);
        boolean registered = userService.addUser(user);
        if (registered) {
            return new ResponseEntity<>(generateJwtResponse(request.getUsername(), request.getPassword()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Wait until the admin accepts the application", HttpStatus.OK);
    }

    private JwtResponse generateJwtResponse(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }
}
