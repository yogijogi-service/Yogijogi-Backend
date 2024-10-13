package com.springboot.yogijogii.data.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResultDto {
    private boolean success;

    private int code;

    private String msg;
    private String detailMessage;
}
