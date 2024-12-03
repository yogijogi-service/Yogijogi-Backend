package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementRequestDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.announcementDto.AnnouncementResponseListDto;
import com.springboot.yogijogii.service.AnnouncementService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("/manager/create")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> createAnnouncement(Long teamId ,AnnouncementRequestDto announcementRequestDto, @RequestPart("image") MultipartFile image, HttpServletRequest request) throws IOException {
        ResultDto resultDto = announcementService.createAnnouncement(teamId,announcementRequestDto, image, request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
    @PutMapping("/manager/update")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> updateAnnouncement(Long announcementId,Long teamId,AnnouncementRequestDto announcementRequestDto, MultipartFile image,HttpServletRequest request)throws IOException {
        ResultDto resultDto = announcementService.updateAnnouncement(announcementId,teamId,announcementRequestDto,image,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @GetMapping("/manager/delete")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<ResultDto> deleteAnnouncement(Long announcementId, Long teamId, HttpServletRequest request){
        ResultDto resultDto = announcementService.deleteAnnouncement(announcementId,teamId,request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
    @GetMapping("/manager/detail")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<AnnouncementResponseDto> getManagerAnnouncementDetails(
            @RequestParam Long teamId,
            @RequestParam Long announcementId,
            HttpServletRequest request) {
        AnnouncementResponseDto announcementResponseDto = announcementService.getManagerAnnouncementDetails(teamId,announcementId,request);
        return ResponseEntity.status(HttpStatus.OK).body(announcementResponseDto);
        }

    @GetMapping("/manager/get-all")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<AnnouncementResponseListDto>> getAllManagerAnnouncements(Long teamId, HttpServletRequest request){
        List<AnnouncementResponseListDto> announcementResponseListDtoList = announcementService.getAllManagerAnnouncements(teamId,request);
        return ResponseEntity.status(HttpStatus.OK).body(announcementResponseListDtoList);
    }
    @GetMapping("/member/detail")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<AnnouncementResponseDto> getAnnouncementDetails(@RequestParam Long teamId, @RequestParam Long announcementId, HttpServletRequest request){
        AnnouncementResponseDto announcementResponseDto = announcementService.getAnnouncementDetails(teamId,announcementId,request);
        return ResponseEntity.status(HttpStatus.OK).body(announcementResponseDto);
    }
    @GetMapping("/member/get-all")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 발급 받은 access_token", required = true, dataType = "String", paramType = "header")
    public ResponseEntity<List<AnnouncementResponseListDto>> getAllAnnouncements(Long teamId, HttpServletRequest request){
        List<AnnouncementResponseListDto> announcementResponseListDtoList = announcementService.getAllAnnouncements(teamId,request);
        return ResponseEntity.status(HttpStatus.OK).body(announcementResponseListDtoList);
    }
    }