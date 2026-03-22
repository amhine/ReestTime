package com.RestTime.RestTime.mapper;

import com.RestTime.RestTime.dto.NotificationResponseDTO;
import com.RestTime.RestTime.model.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user.id")
    NotificationResponseDTO toResponseDTO(Notification notification);
}