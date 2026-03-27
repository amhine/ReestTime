package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.*;

import java.util.List;

public interface UtilisateurService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO createUser(CreateUserRequestDTO request);
    UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request);
    void deleteUser(Long id);

    UserResponseDTO getCurrentUser(String email);
    void changePassword(String email, ChangePasswordRequestDTO request);
    void requestPasswordReset(ForgotPasswordRequestDTO request);
    void resetPassword(String token, String newPassword);
}