package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.AnnouncementDTO;
import com.RestTime.RestTime.dto.AnnouncementCreateDTO;
import com.RestTime.RestTime.dto.UserResponseDTO;
import com.RestTime.RestTime.model.entity.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

    @Mapping(source = "user", target = "author")
    AnnouncementDTO toDTO(Announcement entity);

    Announcement toEntity(AnnouncementCreateDTO dto);
}