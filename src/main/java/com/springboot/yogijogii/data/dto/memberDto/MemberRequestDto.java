package com.springboot.yogijogii.data.dto.memberDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String phoneNum;

    private String email;

    private String password;

    private String passwordCheck;

    private String birtDate;

    private String gender;

}
