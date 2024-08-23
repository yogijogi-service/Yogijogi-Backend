package com.springboot.yogijogii.data.dto.authDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoResponseDto {
    private String name;
    private String phoneNum;
    private String email;
    private String profileUrl;
    private String gender;
    private String birthDate;
}
