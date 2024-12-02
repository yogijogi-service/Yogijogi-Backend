package com.springboot.yogijogii.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.springboot.yogijogii.data.repository.refreshToken.RefreshTokenRepository;
import com.springboot.yogijogii.refreshToken.RefreshToken;
import com.springboot.yogijogii.result.ResultStatusService;
import com.springboot.yogijogii.data.dao.AuthDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dto.authDto.AdditionalInfoDto;
import com.springboot.yogijogii.data.dto.authDto.GoogleResponseDto;
import com.springboot.yogijogii.data.dto.authDto.KakaoResponseDto;
import com.springboot.yogijogii.data.dto.signDto.SignInResultDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.service.AuthService;
import com.springboot.yogijogii.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthDao authDao;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final TeamMemberDao teamMemberDao;
    private final ResultStatusService resultStatusService;
    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${kakao.client.id}")
    private String clientKey;

    @Value("${kakao.redirect.url}")
    private String redirectUrl;

    @Value("${kakao.accesstoken.url}")
    private String kakaoAccessTokenUrl;

    @Value("${kakao.userinfo.url}")
    private String kakaoUserInfoUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect.url}")
    private String googleRedirectUrl;

    @Value("${google.userinfo.url}")
    private String googleUserInfoUrl;

    @Value("${google.accesstoken.url}")
    private String googleAccessTokenUrl;


    @Override
    public SignInResultDto getKakaoUserInfo(String authorizeCode,HttpServletRequest request) {
        log.info("[kakao login] issue a authorizeCode");
        ObjectMapper objectMapper = new ObjectMapper(); //json 파싱 객체
        RestTemplate restTemplate = new RestTemplate(); //client 연결 객체

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientKey);
        params.add("redirect_uri", redirectUrl);
        params.add("code", authorizeCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoAccessTokenUrl,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            log.info("[kakao login] authorizecode issued successfully");
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

            Object accessToken = responseMap.get("access_token");

            return kakao_SignIn((String)accessToken,request);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public SignInResultDto getGoogleUserInfo(String authorizeCode,HttpServletRequest request) {
        log.info("[google login] issue a authorizeCode: {}", authorizeCode);
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUrl);
        params.add("code", authorizeCode);

        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, httpHeaders);
        log.info("[google login] Google Access Token URL: {}", googleAccessTokenUrl);
        log.info("[google login] Request Headers: {}", httpHeaders);
        log.info("[google login] Request Parameters: {}", params);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    googleAccessTokenUrl,
                    HttpMethod.POST,
                    googleTokenRequest,
                    String.class
            );
            log.info("[google login] authorizecode issued successfully");
            log.info("Response: {}", response.getBody()); // 응답 본문 출력
            Map<String,Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            log.info("[Response Map] : {}", responseMap);

            String accessToken = (String) responseMap.get("access_token");
            log.info("[accessToken] : {}", accessToken);

            return google_SignIn(accessToken,request);

        } catch (Exception e) {
            log.error("An error occurred while fetching Google access token", e);
            e.printStackTrace();
            return null;
        }
    }

    private KakaoResponseDto getKakaoInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(kakaoUserInfoUrl, HttpMethod.POST, entity, String.class);

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseMap.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            // 전화번호 변환
            String rawPhoneNumber = (String) kakaoAccount.get("phone_number");
            String formattedPhoneNumber = formatPhoneNumber(rawPhoneNumber);

            KakaoResponseDto responseDto = KakaoResponseDto.builder()
                    .name((String) kakaoAccount.get("name"))
                    .email((String) kakaoAccount.get("email"))
                    .phoneNum(formattedPhoneNumber)
                    .build();

            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private GoogleResponseDto getGoogleInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders httpHeaders = new HttpHeaders();
        log.info("[accessToken] : {}", accessToken);

        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> entity = new HttpEntity<>(requestBody, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(googleUserInfoUrl, HttpMethod.GET, entity, String.class);

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            log.info("[Google User Info Response] : {}", responseMap);

            // emailAddresses 리스트에서 이메일 추출
            List<Map<String, Object>> emailAddresses = (List<Map<String, Object>>) responseMap.get("emailAddresses");
            String email = null;
            if (emailAddresses != null && !emailAddresses.isEmpty()) {
                email = (String) emailAddresses.get(0).get("value");
            }

            if (email == null) {
                log.error("Google 계정에서 이메일을 제공하지 않았습니다.");
                return null;
            }

            GoogleResponseDto googleResponseDto = GoogleResponseDto.builder()
                    .name((String) ((List<Map<String, Object>>) responseMap.get("names")).get(0).get("displayName"))
                    .email(email)
                    .build();

            return googleResponseDto;

        } catch (Exception e) {
            log.error("Google 사용자 정보 조회 중 오류 발생", e);
            return null;
        }
    }

    @Override
    public SignInResultDto kakao_SignIn(String accessToken , HttpServletRequest request) {
        KakaoResponseDto kakaoUserInfoResponse = getKakaoInfo(accessToken);

        SignInResultDto signInResultDto = new SignInResultDto();
        if (kakaoUserInfoResponse == null) {
            return handleSignInFailure(signInResultDto, "카카오 유저 정보를 받아오지 못했습니다.");
        }

        Member member = authDao.findMember(kakaoUserInfoResponse.getEmail());

        if (member == null) {
            member = memberService.createKakaoUser(kakaoUserInfoResponse);
            member.setServiceRole("ROLE_USER");
            request.getSession().setAttribute("partialMember", member);
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setToken(null);
            signInResultDto.setRefreshToken(null);
            signInResultDto.setDetailMessage("추가 정보 입력하세요.");
        } else {
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setNewUser(false);
            signInResultDto.setDetailMessage("로그인 성공.");
            generateAndSetTokens(member,signInResultDto);
        }

        return signInResultDto;

    }


    @Override
    public SignInResultDto google_SignIn(String accessToken,HttpServletRequest request) {
        GoogleResponseDto googleResponseDto = getGoogleInfo(accessToken);

        SignInResultDto signInResultDto = new SignInResultDto();
        if (googleResponseDto == null) {
            return handleSignInFailure(signInResultDto, "Failed to get Kakao user info");
        }

        Member member = authDao.findMember(googleResponseDto.getEmail());

        if (member == null) {
            member = memberService.createGoogleUser(googleResponseDto);
            member.setServiceRole("ROLE_USER");
            request.getSession().setAttribute("partialMember", member);
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setToken(null);
            signInResultDto.setRefreshToken(null);
            signInResultDto.setDetailMessage("추가 정보 입력하세요.");
        } else {
            resultStatusService.setSuccess(signInResultDto);
            signInResultDto.setNewUser(false);
            signInResultDto.setDetailMessage("로그인 성공.");
            generateAndSetTokens(member,signInResultDto);
        }


        return signInResultDto;

    }

    @Override
    public ResponseEntity<?> refreshAccessToken(String refreshToken) {
        if (jwtProvider.validRefreshToken(refreshToken)) {
            String email = jwtProvider.getUsernameFromRefreshToken(refreshToken);
            String newAccessToken = jwtProvider.createToken(email, List.of("ROLE_USER"));

            // 리프레시 토큰의 유효 기간이 하루 미만일 경우, 새로운 리프레시 토큰을 발급
            if (jwtProvider.getExpirationDuration(refreshToken) < 1000 * 60 * 60 * 24) {
                String newRefreshToken = jwtProvider.createRefreshToken(email);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken));
            } else {
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }
    }

    @Override
    public SignInResultDto saveAdditionalInfo(AdditionalInfoDto additionalInfoDto , HttpServletRequest request) {
        Member member = (Member) request.getSession().getAttribute("partialMember");
        SignInResultDto signInResultDto = new SignInResultDto();

        if (member != null) {
            member.addAuthAdditionalInfo(additionalInfoDto); // 기존 User 객체를 전달하여 새로운 User 객체 생성
            authDao.saveMember(member);
            addServiceRoleManager(member);
            generateAndSetTokens(member,signInResultDto);
            signInResultDto.setNewUser(true);
            signInResultDto.setDetailMessage("회원가입 완료");
            resultStatusService.setSuccess(signInResultDto);
        } else {
            resultStatusService.setFail(signInResultDto);
        }
        return signInResultDto;
    }

    private void generateAndSetTokens(Member member, SignInResultDto signInResultDto) {
        String accessTokenNew = jwtProvider.createToken(member.getEmail(), List.of("ROLE_USER"));

        String refreshToken = jwtProvider.createRefreshToken(member.getEmail());

        refreshTokenRepository.save(new RefreshToken(member.getEmail(),refreshToken));
        authDao.saveMember(member);

        log.info("[refreshToken] : {}",refreshToken);
        signInResultDto.setToken(accessTokenNew);
        signInResultDto.setRefreshToken(refreshToken);
        resultStatusService.setSuccess(signInResultDto);
    }
    private SignInResultDto handleSignInFailure(SignInResultDto signInResultDto, String errorMessage) {
        resultStatusService.setFail(signInResultDto);
        signInResultDto.setDetailMessage(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    private void addServiceRoleManager(Member member){
        ServiceRole serviceRole = new ServiceRole();
        serviceRole.setMember(member);
        serviceRole.setRole("Role_User");
        teamMemberDao.saveServiceRole(serviceRole);
    }

    private String formatPhoneNumber(String rawPhoneNumber) {
        if (rawPhoneNumber == null || !rawPhoneNumber.startsWith("+82")) {
            return rawPhoneNumber; // 변환이 필요 없는 경우 원본 반환
        }
        rawPhoneNumber = rawPhoneNumber.replaceAll("\\s+", "");
        // +82 -> 0
        String localPhoneNumber = rawPhoneNumber.replace("+82", "0");

        return localPhoneNumber;
    }



}
