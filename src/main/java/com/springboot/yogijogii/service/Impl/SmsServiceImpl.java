package com.springboot.yogijogii.service.Impl;


import com.springboot.yogijogii.redis.SmsCertification;
import com.springboot.yogijogii.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService {

    private final SmsCertification smsCertification;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String secretKey;

    @Value("${coolsms.fromnumber}")
    private String fromnumber;


    @Override
    public String createRandomNumber() {
        Random random = new Random();
        String randomNum = "";
        for(int i =0;i<4;i++){
            String rand = Integer.toString(random.nextInt(10));
            randomNum += rand;
        }

        return randomNum;
    }

    @Override
    public HashMap<String, String> makeParam(String to, String randomNum) {
        HashMap<String,String> param = new HashMap<>();
        param.put("to", to);
        param.put("from", fromnumber);
        param.put("type","SMS");
        param.put("text","[요기조기] 인증번호 입니다. \n" + randomNum + " 입력하세요. " ) ;
        param.put("app_version", "yogijogiTest1.1");
        System.out.println(param);
        return param;
    }

    @Override
    public ResponseEntity<Map<String, String>> sendSMS(String phoneNum, HttpServletRequest request) {
        Message message = new Message(apiKey,secretKey);
        
        //인증번호 생성
        String randomNum = createRandomNumber();
        log.info("[randomNum] : {}", randomNum);

        request.getSession().setAttribute("partial_phoneNum",phoneNum);

        //발신 정보 설정
        HashMap<String,String> param = makeParam(phoneNum,randomNum);

        try{
            JSONObject obj = message.send(param);
            log.info("[obj] : {}", obj);
            System.out.println(obj.toString());
        }catch (CoolsmsException e){
            System.out.println(e.getCode());
            System.out.println(e.getMessage());
        }
        //redis에 저장
        smsCertification.createSmsCertification(phoneNum,String.valueOf(randomNum));
        log.info("[sms] phoneNum: {} , randomNum: {}", phoneNum, randomNum);

        //응답 반환
        Map<String,String> response = new HashMap<>();
        response.put("message" , "문자 전송 완료");
        response.put("certificationNum",randomNum);
        return ResponseEntity.ok(response);

    }



    @Override
    public boolean verifyNumber(String certification, HttpServletRequest request) {
        request.getSession().setAttribute("certification",certification);
        String phoneNum = (String) request.getSession().getAttribute("partial_phoneNum");

        if(smsCertification.haskey(phoneNum)&&smsCertification.getSmsCertification(phoneNum).equals(certification)){
            smsCertification.deleteSmsCertification(phoneNum);
            return true;
        }else {
            return false;
        }
    }
}
