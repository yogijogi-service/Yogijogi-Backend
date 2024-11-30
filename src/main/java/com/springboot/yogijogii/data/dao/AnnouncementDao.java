package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementRequestDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseListDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;

import java.util.List;

public interface AnnouncementDao {
    void save(AnnouncementRequestDto announcementRequestDto, String image, Member member, Team team);
    void update(AnnouncementRequestDto announcementRequestDto, Long announcementId, String image, Member member);
    void delete(Long id);
    List<AnnouncementResponseListDto> getAnnouncementByTeamId(Long teamId);

}
