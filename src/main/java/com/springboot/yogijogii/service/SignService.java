package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.signDto.AgreementDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;

import javax.servlet.http.HttpServletRequest;

public interface SignService {
    ResultDto SignUpSmsVerify(String certificationNumber, HttpServletRequest request);

    ResultDto SignUp (SignReqeustDto signReqeustDto, AgreementDto agreementDto, HttpServletRequest request);


    SignInResultDto SignIn(String email, String password);

}
