package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.Result.ResultStatusService;
import com.springboot.yogijogii.S3.S3Uploader;
import com.springboot.yogijogii.data.dao.AnnouncementDao;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementRequestDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.entity.Announcement;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.announcement.AnnouncementRepository;
import com.springboot.yogijogii.data.repository.team.TeamRepository;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final S3Uploader s3Uploader;
    private final AnnouncementDao announcementDao;
    private final ResultStatusService resultStatusService;
    private final TeamRepository teamRepository;
    private final AnnouncementRepository announcementRepository;


    @Override
    public ResultDto createAnnouncement(Long teamId ,AnnouncementRequestDto announcementRequestDto, MultipartFile image, HttpServletRequest request) throws IOException {
        Member member = jwtAuthenticationService.authenticationToken(request);
        log.info("[memberEmail] : {} ",member.getEmail());
        ResultDto resultDto = new ResultDto();

        Team team = teamRepository.findByTeamId(teamId);
        log.info("[team] : {} ",team.getTeamName());
        if(team == null){
            throw new RuntimeException("팀을 찾을 수 없습니다.");
        }

        // 이미지가 null이 아니면 S3에 업로드
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = saveImageS3(image);
        }
        if(isMemberManagerOfTeam(member,team)){
            Announcement announcement = new Announcement(
                    announcementRequestDto.getTitle(),
                    announcementRequestDto.getContent(),
                    imageUrl,
                    member
            );
            announcementDao.save(announcement);
            resultDto.setDetailMessage("공지사항 등록 완료");
            resultStatusService.setSuccess(resultDto);
        }else{
            resultDto.setDetailMessage("공지사항 등록 권한이 없습니다.");
            resultStatusService.setFail(resultDto);
        }


        return resultDto;
    }

    @Override
    public AnnouncementResponseDto getManagerAnnouncementDetails(Long teamId, Long announcementId,HttpServletRequest request) {
        Member member = jwtAuthenticationService.authenticationToken(request);
        log.info("[memberEmail] : {} ",member.getEmail());

        Team team = teamRepository.findByTeamId(teamId);
        log.info("[team] : {} ",team.getTeamName());

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(()-> new ResourceNotFoundException("해당 ID로 공지사항을 찾을 수 없습니다. ID: " + announcementId));


        if(isMemberManagerOfTeam(member,team)){
            AnnouncementResponseDto announcementResponseDto = new AnnouncementResponseDto(
                    announcementId,
                    member.getName(),
                    announcement.getTitle(),
                    announcement.getContent(),
                    announcement.getImageUrl(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            return announcementResponseDto;
        }else{

            throw new RuntimeException("공지사랑 열람할 권한이 없습니다.");
        }

    }

    public String saveImageS3(MultipartFile profileImage) throws IOException {
        if (profileImage != null && !profileImage.isEmpty()) {
            return s3Uploader.uploadImage(profileImage, "image/announcement");
        }
        return null;
    }

    private boolean isMemberManagerOfTeam(Member member, Team team) {
        return member.getTeamMembers().stream()
                .anyMatch(teamMember ->
                        teamMember.getTeam().equals(team) &&
                                teamMember.getRole().equals("ROLE_MANAGER"));
    }
}
