package com.springboot.yogijogii.data.dto.signDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends ResultDto {

  private String token;
  private String refreshToken;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token,String refreshToken,String detailMessage) {
        super(success, code, msg,detailMessage);
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
