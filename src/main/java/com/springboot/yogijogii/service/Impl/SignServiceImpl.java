package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.MemberRoleDao;
import com.springboot.yogijogii.data.dao.SignDao;
import com.springboot.yogijogii.data.dto.CommonResponse;
import com.springboot.yogijogii.data.dto.signDto.AgreementDto;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import com.springboot.yogijogii.data.dto.signDto.SignReqeustDto;
import com.springboot.yogijogii.data.entity.*;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.jwt.Impl.JwtProvider;
import com.springboot.yogijogii.service.MemberService;
import com.springboot.yogijogii.service.SignService;
import com.springboot.yogijogii.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignServiceImpl implements SignService {
    private final SmsService smsService;
    private final MemberService memberService;
    private final SignDao signDao;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberRoleDao memberRoleDao;

    @Override
    public ResultDto SignUpSmsVerify(String certificationNumber, HttpServletRequest request) {
        ResultDto resultDto = new ResultDto();

        try {
            // 세션에서 부분 전화번호를 가져옴
            String partialPhoneNum = (String) request.getSession().getAttribute("partial_phoneNum");
            if (partialPhoneNum == null) {
                throw new IllegalStateException("세션에 partial_phoneNum이 존재하지 않습니다.");
            }

            // 인증 번호 확인
            if (smsService.verifyNumber(certificationNumber, request)) {
                // 멤버 객체를 생성하고 세션에 저장
                Member member = memberService.saveVerifyInfo(partialPhoneNum);
                request.getSession().setAttribute("partialMember", member);

                resultDto.setDetailMessage("인증이 완료되었습니다.");
                setSuccess(resultDto);
            } else {
                resultDto.setDetailMessage("인증 실패하였습니다.");
                setFail(resultDto);
            }
        //예외처리
        } catch (IllegalStateException e) {
            resultDto.setDetailMessage("세션 데이터가 누락되었습니다: " + e.getMessage());
            setFail(resultDto);
            e.printStackTrace();
        } catch (Exception e) {
            resultDto.setDetailMessage("시스템 오류가 발생하였습니다. 나중에 다시 시도해 주세요.");
            setFail(resultDto);
            e.printStackTrace();
        }

        return resultDto;
    }

    @Override
    public ResultDto SignUp(SignReqeustDto signReqeustDto, AgreementDto agreementDto, HttpServletRequest request) {
        ResultDto resultDto = new ResultDto();
        Member partialMember = (Member) request.getSession().getAttribute("partialMember");

        if(partialMember !=null) {
            Member member = memberService.createUser(signReqeustDto);
            MemberAgreement memberAgreement = memberService.saveAgreement(agreementDto);

            member.setPhoneNum(partialMember.getPhoneNum()); // 세션에서 가져온 전화번호를 명시적으로 할당함.
            member.setMemberAgreement(memberAgreement);
            memberAgreement.setMember(member);

            //디비 저장이용
            signDao.saveSignUpInfo(member);
            signDao.saveMemberAgree(memberAgreement);
            addServiceRoleManager(member);

            resultDto.setDetailMessage("회원가입 완료.");
            setSuccess(resultDto);
        }else{
            resultDto.setDetailMessage("회원가입 실패.");
            setFail(resultDto);
        }
        return resultDto;
    }

    @Override
    public SignInResultDto SignIn(String email, String password) {
        Member member = memberRepository.getByEmail(email);

        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw  new RuntimeException("Invalid credentials");
        }

        log.info("[getSignInResult] 패스워드 일치");
        // 토큰 생성
        String accessToken = jwtProvider.createToken(
                String.valueOf(member.getEmail()),
                member.getMemberRoles().stream()
                        .map(MemberRole::getRole)
                        .collect(Collectors.toList())
        );

        String existingRefreshToken = member.getRefreshToken();
        String refreshToken;

        if(existingRefreshToken != null && jwtProvider.validRefreshToken(existingRefreshToken)) {
            refreshToken = existingRefreshToken;
        }else{
            refreshToken = jwtProvider.createRefreshToken(member.getEmail());
            member.setRefreshToken(refreshToken);
            signDao.saveSignUpInfo(member);
        }

        // SignInResultDto 작성 및 반환
        SignInResultDto signInResultDto = new SignInResultDto();
        signInResultDto.setToken(accessToken);
        signInResultDto.setRefreshToken(refreshToken);
        signInResultDto.setDetailMessage("로그인 성공");
        setSuccess(signInResultDto);
        return signInResultDto;
    }


    private void setSuccess(ResultDto resultDto){
        resultDto.setSuccess(true);
        resultDto.setCode(CommonResponse.SUCCESS.getCode());
        resultDto.setMsg(CommonResponse.SUCCESS.getMsg());

    }
    private void setFail(ResultDto resultDto){
        resultDto.setSuccess(false);
        resultDto.setCode(CommonResponse.Fail.getCode());
        resultDto.setMsg(CommonResponse.Fail.getMsg());
        throw new IllegalArgumentException("인증 실패");
    }

    private void addServiceRoleManager(Member member) {
        if (member.getServiceRoles() == null) {
            member.setServiceRoles(new ArrayList<>());
        }
        ServiceRole serviceRole = new ServiceRole();
        serviceRole.setMember(member);
        serviceRole.setRole("ROLE_USER"); // 권장되는 권한 이름으로 변경
        member.getServiceRoles().add(serviceRole);
        memberRoleDao.saveServiceRole(serviceRole);
        // member의 serviceRole 필드를 설정하여 ID가 저장되도록 함
        // member의 serviceRole 필드도 설정
        member.setServiceRole("ROLE_USER");
        memberRoleDao.saveServiceRole(serviceRole);


    }
}
