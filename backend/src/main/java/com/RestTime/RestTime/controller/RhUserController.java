package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.CreateUserRequestDTO;
import com.RestTime.RestTime.dto.UpdateUserRequestDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rh/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RH')")
public class RhUserController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(utilisateurService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(utilisateurService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO request) {
        UserResponseDTO createdUser = utilisateurService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequestDTO request) {
        return ResponseEntity.ok(utilisateurService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        utilisateurService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
