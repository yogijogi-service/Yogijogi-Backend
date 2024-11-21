package com.springboot.yogijogii.dateConverter;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateTimeConverter {

    public LocalDateTime convertToDateTime(String inputDateTime) {

        // 현재 연도를 추가하여 LocalDateTime으로 변환
        String currentYear = String.valueOf(LocalDateTime.now().getYear());
        inputDateTime = currentYear + " " + inputDateTime;

        DateTimeFormatter extendedFormatter = DateTimeFormatter.ofPattern("yyyy MM월 dd일 HH시 mm분");
        return LocalDateTime.parse(inputDateTime, extendedFormatter);
    }
}
