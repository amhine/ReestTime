package com.RestTime.RestTime.repository;

import com.RestTime.RestTime.model.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findAllByOrderByDateCreationDesc();
}
