package com.springboot.yogijogii.data.dto.announcementDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class AnnouncementRequestDto {
    private String title;
    private String content;
}
