package com.springboot.yogijogii.data.repository.announcement;

import com.springboot.yogijogii.data.entity.Announcement;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepositoryCustom {
    Optional<List<Announcement>> getAnnouncementByTeamId(Long teamId);
}
