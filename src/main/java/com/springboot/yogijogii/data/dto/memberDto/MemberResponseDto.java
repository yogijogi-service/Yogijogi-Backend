package com.springboot.yogijogii.data.dto.memberDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private Long memberId;  // id 식별값

    private String email;

    private String gender;

    private String name;

    private String address;

    private String profileUrl;

    private String birthDate;  //생년월일

    private String phoneNum;

    private String level;

    private boolean hasExperience;  // 선수경험

    private String loginMethod;

    private String create_At;

    private String update_At;
}
