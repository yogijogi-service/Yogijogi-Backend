package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.AnnouncementDao;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementRequestDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseListDto;
import com.springboot.yogijogii.data.entity.Announcement;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.announcement.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementDaoImpl implements AnnouncementDao {

    private final AnnouncementRepository announcementRepository;

    @Override
    public void save(AnnouncementRequestDto announcementRequestDto, String imageUrl, Member member, Team team) {
        Announcement announcement = new Announcement(
                announcementRequestDto.getTitle(),
                announcementRequestDto.getContent(),
                imageUrl,
                member,
                team
        );
        announcementRepository.save(announcement);
    }
    @Override
    public void update(AnnouncementRequestDto announcementRequestDto, Long announcementId,String imageUrl, Member member) {
        Announcement announcement = announcementRepository.findById(announcementId).get();
        if(announcementRequestDto.getTitle() != null) {
            announcement.setTitle(announcementRequestDto.getTitle());
        }
        if(announcementRequestDto.getContent() != null) {
            announcement.setContent(announcementRequestDto.getContent());
        }
        if (imageUrl != null) {
            announcement.setImageUrl(imageUrl);
        }
        if(member.getMemberId() != null) {
            announcement.setMember(member);
        }
        announcement.setUpdatedAt(LocalDateTime.now());
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        announcementRepository.deleteById(id);
    }

    @Override
    public List<AnnouncementResponseListDto> getAnnouncementByTeamId(Long teamId) {

        Optional<List<Announcement>> announcementListInfo = announcementRepository.getAnnouncementByTeamId(teamId);
        List<Announcement> announcementList = announcementListInfo.get();
        List<AnnouncementResponseListDto> announcementListDtos = new ArrayList<>();
        for(Announcement announcement : announcementList) {
            AnnouncementResponseListDto announcementResponseListDto = new AnnouncementResponseListDto(
                    announcement.getId(),
                    announcement.getTitle(),
                    announcement.getCreatedAt()
            );
            announcementListDtos.add(announcementResponseListDto);
        }
        return announcementListDtos;
    }
}
