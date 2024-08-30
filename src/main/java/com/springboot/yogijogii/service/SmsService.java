package com.springboot.yogijogii.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public interface SmsService {
    HashMap<String,String> makeParam(String to, String randomNum);
    String createRandomNumber();
    ResponseEntity<Map<String,String>> sendSMS(String phoneNum, HttpServletRequest request);
    boolean verifyNumber(String certification,HttpServletRequest request);
}
