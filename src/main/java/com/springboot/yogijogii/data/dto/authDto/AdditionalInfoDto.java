package com.springboot.yogijogii.data.dto.authDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdditionalInfoDto {

    private boolean hasExperience;
    private String level;
    private String phoneNum;
    private String gender;
    private String birthDate;
    private String address;
}
