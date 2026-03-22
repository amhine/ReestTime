package com.RestTime.RestTime.service;

import com.RestTime.RestTime.dto.AnnouncementCreateDTO;
import com.RestTime.RestTime.dto.AnnouncementDTO;
import java.util.List;


public interface AnnouncementService {


     AnnouncementDTO createAnnouncement(AnnouncementCreateDTO dto) ;


    AnnouncementDTO updateAnnouncement(Long id, AnnouncementCreateDTO dto) ;

     List<AnnouncementDTO> getAllAnnouncements() ;

     void deleteAnnouncement(Long id) ;
}