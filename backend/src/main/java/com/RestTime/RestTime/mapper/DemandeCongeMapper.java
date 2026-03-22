package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.DemandeCongeCreateDTO;
import com.RestTime.RestTime.dto.DemandeCongeResponseDTO;
import com.RestTime.RestTime.model.entity.DemandeConge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DemandeCongeMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "nom", source = "user.nom")
    @Mapping(target = "prenom", source = "user.prenom")
    DemandeCongeResponseDTO toResponseDTO(DemandeConge demande);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombreJours", ignore = true)
    @Mapping(target = "dateSoumission", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "historiques", ignore = true)
    @Mapping(target = "pieceJointe", ignore = true)
    DemandeConge toEntity(DemandeCongeCreateDTO dto);
}