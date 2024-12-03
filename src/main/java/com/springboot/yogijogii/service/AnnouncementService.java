package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementRequestDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseListDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface AnnouncementService {
    ResultDto createAnnouncement(Long teamId ,AnnouncementRequestDto announcementRequestDto, MultipartFile image, HttpServletRequest request) throws IOException;
    ResultDto deleteAnnouncement(Long announcementId, Long teamId ,HttpServletRequest request);
    ResultDto updateAnnouncement(Long id, Long teamId,AnnouncementRequestDto announcementRequestDto, MultipartFile image,HttpServletRequest request)throws IOException;
    AnnouncementResponseDto getManagerAnnouncementDetails(Long teamId, Long announcementId,HttpServletRequest request);
    List<AnnouncementResponseListDto> getAllManagerAnnouncements(Long teamId, HttpServletRequest request);
    AnnouncementResponseDto getAnnouncementDetails(Long teamId, Long announcementId,HttpServletRequest request);
    List<AnnouncementResponseListDto> getAllAnnouncements(Long teamId, HttpServletRequest request);

}
