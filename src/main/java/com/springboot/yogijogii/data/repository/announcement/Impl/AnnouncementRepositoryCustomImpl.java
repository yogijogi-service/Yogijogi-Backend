package com.springboot.yogijogii.data.repository.announcement.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogii.data.entity.Announcement;
import com.springboot.yogijogii.data.entity.QAnnouncement;
import com.springboot.yogijogii.data.repository.announcement.AnnouncementRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AnnouncementRepositoryCustomImpl implements AnnouncementRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    QAnnouncement qAnnouncement = QAnnouncement.announcement;

    @Override
    public Optional<List<Announcement>> getAnnouncementByTeamId(Long teamId) {

        return Optional.ofNullable(jpaQueryFactory.selectFrom(qAnnouncement)
                .where(qAnnouncement.team().teamId.eq(teamId))
                .fetch());
    }
}
