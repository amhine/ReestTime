package com.RestTime.RestTime.controller;

import com.RestTime.RestTime.dto.ChangePasswordRequestDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(utilisateurService.getCurrentUser(principal.getName()));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDTO request,
            Principal principal) {

        utilisateurService.changePassword(principal.getName(), request);
        return ResponseEntity.ok().build();
    }
}