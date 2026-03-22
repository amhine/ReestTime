package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.AuthResponse;
import com.RestTime.RestTime.dto.CreateUserRequestDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    AuthResponse toAuthResponse(String token);

    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "demandes", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "telephone", ignore = true)
    @Mapping(target = "adresse", ignore = true)
    @Mapping(target = "photoProfile", ignore = true)
    @Mapping(target = "poste", ignore = true)
    User toEntity(CreateUserRequestDTO request);
}