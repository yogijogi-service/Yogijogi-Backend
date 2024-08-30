package com.springboot.yogijogii.service;

import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;

import javax.servlet.http.HttpServletRequest;

public interface SignService {
    ResultDto SignUpSmsVerify(String certificationNumber, HttpServletRequest request);

    ResultDto SignUp (SignReqeustDto signReqeustDto, HttpServletRequest request);


    SignInResultDto SignIn(String email, String password);

}
