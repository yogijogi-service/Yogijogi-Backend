package com.springboot.yogijogii.data.dto.signDto;

import com.springboot.yogijogii.data.dto.ResultDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDto extends ResultDto {

  private String token;
  private String refreshToken;
  private boolean isNewUser;

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token,String refreshToken,String detailMessage) {
        super(success, code, msg,detailMessage);
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
