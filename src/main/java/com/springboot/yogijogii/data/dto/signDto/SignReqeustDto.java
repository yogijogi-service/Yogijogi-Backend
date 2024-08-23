package com.springboot.yogijogii.data.dto.signDto;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;

public class SignReqeustDto {
    private String password;

    private String gender;

    private String name;

    private String profileUrl;

    private String birthDate;  //생년월일

    private String phoneNum;

    private String level;

    private boolean certificationNum;   // 인증번호

    private boolean hasExperience;  // 선수경험

    private List<String> position;   //포지션

    private List<String> availableDays ;   //가능 요일

    private String availableTimeStart;  // 시작 시간

    private String  availableTimeEnd;  // 종료 시간

    boolean allAgreement;

    boolean consentServiceUser;

    boolean consentPersonalInfo;

    boolean consentToThirdPartyOffers;
}
