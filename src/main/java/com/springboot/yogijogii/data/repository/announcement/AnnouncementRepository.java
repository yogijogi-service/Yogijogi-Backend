package com.springboot.yogijogii.data.repository.announcement;

import com.springboot.yogijogii.data.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> ,AnnouncementRepositoryCustom{
}
