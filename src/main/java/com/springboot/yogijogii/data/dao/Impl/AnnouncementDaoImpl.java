package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.AnnouncementDao;
import com.springboot.yogijogii.data.entity.Announcement;
import com.springboot.yogijogii.data.repository.announcement.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnouncementDaoImpl implements AnnouncementDao {

    private final AnnouncementRepository announcementRepository;

    @Override
    public void save(Announcement announcement) {
        announcementRepository.save(announcement);
    }
}
