package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;
import com.springboot.yogijogii.service.SignService;
import com.springboot.yogijogii.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sign")
@Slf4j
public class SignController {

    private final SignService signService;
    private final SmsService smsService;

    @PostMapping("/send-sms")
    public ResponseEntity<Map<String,String>> sendSms(String phoneNum,HttpServletRequest request) {
        try{
            ResponseEntity<Map<String,String>> response = smsService.sendSMS(phoneNum,request);
            log.info("[문자 인증 진행중] phoneNumber: {}, randonNum {}",phoneNum,response.getBody().get("randonNum"));
            return response;
        }catch (Exception e){
            log.info("[문자 인증 실패] phoneNumber: {}, randonNum {}",phoneNum,e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","문자 전송 실패"));
        }
    }


    @PostMapping("/verify")
    ResponseEntity<ResultDto> SignUpSmsVerify(String certificationNumber, HttpServletRequest request){
        ResultDto resultDto = signService.SignUpSmsVerify(certificationNumber, request);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }

    @PostMapping("/sign-up")
    ResponseEntity<ResultDto> SignUp (SignReqeustDto signReqeustDto, HttpServletRequest request){
        ResultDto resultDto = signService.SignUp(signReqeustDto, request);
        return ResponseEntity.status(resultDto.isSuccess()?HttpStatus.OK:HttpStatus.BAD_REQUEST).body(resultDto);
    }

    @PostMapping("/sign-in")
    ResponseEntity<SignInResultDto> SignIn(String email, String password){
        SignInResultDto signInResultDto = signService.SignIn(email, password);
        return ResponseEntity.status(HttpStatus.OK).body(signInResultDto);
    }


}
