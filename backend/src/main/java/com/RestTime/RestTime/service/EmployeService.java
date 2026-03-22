package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.SoldeCongeDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.dto.UpdateUserRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeService {

    SoldeCongeDTO getSoldeConges(Long userId);

    void annulerDemande(Long demandeId, Long userId);

    UserResponseDTO updateProfile(Long userId, UpdateUserRequestDTO dto, MultipartFile photo);

}