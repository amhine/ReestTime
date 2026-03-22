package com.RestTime.RestTime.service.impl;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.LoginRequest;
import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.Role;
import com.RestTime.RestTime.repository.UserRepository;
import com.RestTime.RestTime.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@resttime.com");
        user.setMotDePasse("password123");
        user.setRole(Role.EMPLOYE);

        loginRequest = new LoginRequest("test@resttime.com", "password123");
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByEmail("test@resttime.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("fake-jwt-token");

        AuthResponse response = authService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("test@resttime.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> 
            authService.authenticate(loginRequest)
        );
    }
}
