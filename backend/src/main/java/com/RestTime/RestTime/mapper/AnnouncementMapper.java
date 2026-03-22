package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.AnnouncementDTO;
import com.RestTime.RestTime.dto.AnnouncementCreateDTO;
import com.RestTime.RestTime.model.entity.Announcement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

    AnnouncementDTO toDTO(Announcement entity);

    Announcement toEntity(AnnouncementCreateDTO dto);
}