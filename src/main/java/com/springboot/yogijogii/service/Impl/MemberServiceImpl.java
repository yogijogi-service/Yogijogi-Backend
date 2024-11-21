package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dto.authDto.GoogleResponseDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.memberDto.MemberRequestDto;
import com.springboot.yogijogii.data.dto.memberDto.MemberResponseDto;
import com.springboot.yogijogii.data.dto.signDto.AgreementDto;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberAgreement;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.jwt.JwtAuthenticationService;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.result.ResultStatusService;
import com.springboot.yogijogii.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberDao memberDao;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final ResultStatusService resultStatusService;

    @Override
    public  Member createKakaoUser(KakaoResponseDto kakaoUserInfoResponse) {
        return Member.builder()
                .email(kakaoUserInfoResponse.getEmail())
                .name(kakaoUserInfoResponse.getName())
                .phoneNum(kakaoUserInfoResponse.getPhoneNum())
                .loginMethod("Kakao")
                .password("pass")
                .passwordCheck("pass")
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }

    @Override
    public Member createGoogleUser(GoogleResponseDto googleResponseDto) {
        return Member.builder()
                .name(googleResponseDto.getName())
                .email(googleResponseDto.getEmail())
                .profileUrl(googleResponseDto.getProfileUrl())
                .loginMethod("Google")
                .password("pass")
                .passwordCheck("pass")
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }

    @Override
    public  Member createUser(SignReqeustDto signReqeustDto) {
        // 비밀번호 일치 여부 확인
        if (!signReqeustDto.getPassword().equals(signReqeustDto.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        return Member.builder()
                .email(signReqeustDto.getEmail())
                .password(passwordEncoder.encode(signReqeustDto.getPassword()))
                .passwordCheck(passwordEncoder.encode(signReqeustDto.getPasswordCheck()))
                .birthDate(signReqeustDto.getBirthDate())
                .level(signReqeustDto.getLevel())
                .hasExperience(signReqeustDto.isHasExperience())
                .certificationNum(true)
                .address(signReqeustDto.getBirthDate())
                .loginMethod("Normal")
                .gender(signReqeustDto.getGender())
                .name(signReqeustDto.getName())
                .address(signReqeustDto.getAddress())
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }
    @Override
    public  Member saveVerifyInfo(String phoneNum) {
        return Member.builder()
                .phoneNum(phoneNum)
                .certificationNum(true)
                .build();
    }

    public MemberAgreement saveAgreement(AgreementDto agreementDto) {
        return MemberAgreement.builder()
                .allAgreement(agreementDto.isAllAgreement())
                .consentPersonalInfo(agreementDto.isConsentPersonalInfo())
                .consentServiceUser(agreementDto.isConsentServiceUser())
                .consentToReceivingMail(agreementDto.isConsentToReceivingMail())
                .consentToThirdPartyOffers(agreementDto.isConsentToThirdPartyOffers())
                .build();

    }

    @Override
    public MemberResponseDto getUser(HttpServletRequest servletRequest) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);
        if(member != null){
            MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                    .memberId(member.getMemberId())
                    .address(member.getAddress())
                    .birthDate(member.getBirthDate())
                    .gender(member.getGender())
                    .email(member.getEmail())
                    .level(member.getLevel())
                    .hasExperience(member.isHasExperience())
                    .loginMethod(member.getLoginMethod())
                    .name(member.getName())
                    .phoneNum(member.getPhoneNum())
                    .profileUrl(member.getProfileUrl())
                    .create_At(String.valueOf(member.getCreate_At()))
                    .update_At(String.valueOf(member.getUpdate_At()))
                    .build();
            return memberResponseDto;
        }
        return null;
    }

    @Override
    public ResultDto updateUser(HttpServletRequest servletRequest, MemberRequestDto requestDto) {
        Member member = jwtAuthenticationService.authenticationToken(servletRequest);
        ResultDto resultDto = new ResultDto();
        if(requestDto.getPassword().equals(requestDto.getPasswordCheck())){
            if(member!=null){
                member.builder()
                        .phoneNum(requestDto.getPhoneNum())
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .passwordCheck(passwordEncoder.encode(requestDto.getPasswordCheck()))
                        .birthDate(requestDto.getBirtDate())
                        .gender(requestDto.getGender())
                        .build();
                memberDao.save(member);
                resultStatusService.setSuccess(resultDto);
                resultDto.setDetailMessage("프로필 수정을 완료하였습니다.");
            }
        }else {
            resultStatusService.setFail(resultDto);
            resultDto.setDetailMessage("비밀번호가 일치하지 않습니다.");
        }
        return resultDto;
    }

    private void saveTeamMember(Member member, Team team , String role){
        TeamMember teamMember = new TeamMember();
        teamMember.setMember(member);
        teamMember.setTeam(team);
        teamMember.setRole(role);
    }
}
