package com.springboot.yogijogii.data.dto.announcementDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnnouncementResponseListDto {
    private Long id;
    private String title;
    private LocalDateTime createAt;
}
