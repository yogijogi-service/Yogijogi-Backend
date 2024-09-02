package com.springboot.yogijogii.controller;

import com.springboot.yogijogii.data.dto.memberDto.MemberRequestDto;
import com.springboot.yogijogii.data.dto.memberDto.MemberResponseDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.service.MemberService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/getUser")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<MemberResponseDto> getUser(HttpServletRequest servletRequest) {
        MemberResponseDto responseDto = memberService.getUser(servletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/updateUser")
    @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    ResponseEntity<ResultDto> updateUser(HttpServletRequest servletRequest,
                                           @RequestBody MemberRequestDto requestDto) {
        ResultDto resultDto = memberService.updateUser(servletRequest, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(resultDto);
    }
}
