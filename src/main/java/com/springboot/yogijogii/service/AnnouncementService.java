package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementRequestDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.entity.Announcement;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AnnouncementService {
    ResultDto createAnnouncement(Long teamId ,AnnouncementRequestDto announcementRequestDto, MultipartFile image, HttpServletRequest request) throws IOException;
    AnnouncementResponseDto getManagerAnnouncementDetails(Long teamId, Long announcementId,HttpServletRequest request);

}
