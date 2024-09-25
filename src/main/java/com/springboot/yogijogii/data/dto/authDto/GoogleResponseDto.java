package com.springboot.yogijogii.data.dto.authDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GoogleResponseDto {
    private String name;
    private String email;
    private String profileUrl;
}
