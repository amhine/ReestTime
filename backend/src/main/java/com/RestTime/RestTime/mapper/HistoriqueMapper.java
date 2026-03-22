package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.HistoriqueResponseDTO;
import com.RestTime.RestTime.model.entity.Historique;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface  HistoriqueMapper {
    @Mapping(source = "demandeConge.id", target = "demandeId")
    @Mapping(source = "demandeConge.user.id", target = "userId")
    HistoriqueResponseDTO toResponseDTO(Historique historique);
}
