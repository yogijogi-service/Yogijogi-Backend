package com.springboot.yogijogii.result;


import com.springboot.yogijogii.data.dto.CommonResponse;
import com.springboot.yogijogii.data.dto.ResultDto;
import org.springframework.stereotype.Service;

@Service
public class ResultStatusService {

    public void setSuccess(ResultDto resultDto) {
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());

    }

    public void setFail(ResultDto resultDto) {
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
    }
}
