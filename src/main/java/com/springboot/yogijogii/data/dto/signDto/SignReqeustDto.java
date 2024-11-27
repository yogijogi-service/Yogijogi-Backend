package com.springboot.yogijogii.data.dto.signDto;

import lombok.Data;

import java.util.List;

@Data
public class SignReqeustDto {
    private String password;

    private String passwordCheck;

    private String gender;

    private String name;

    private String email;

    private String birthDate;  //생년월일

    private String level;

    private String address;

    private String addressDetail;

    private boolean hasExperience;


}
