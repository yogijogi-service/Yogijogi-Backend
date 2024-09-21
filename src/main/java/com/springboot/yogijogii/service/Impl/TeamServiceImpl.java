package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.S3.S3Uploader;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.MemberRoleDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dto.CommonResponse;
import com.springboot.yogijogii.data.dto.signDto.ResultDto;
import com.springboot.yogijogii.data.dto.teamDto.CreateTeamRquestDto;
import com.springboot.yogijogii.data.dto.teamDto.TeamResponseDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberRole;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {

    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final JwtProvider jwtProvider;
    private final TeamDao teamDao;
    private final MemberDao memberDao;
    private final MemberRoleDao memberRoleDao;

    @Override
    public ResultDto createTeam(CreateTeamRquestDto createTeamRquestDto, MultipartFile image,HttpServletRequest request) throws IOException{
        String token = jwtProvider.resolveToken(request);
        if (token == null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setDetailMessage("토큰이 요청에 포함되지 않았습니다.");
            log.warn("[createTeam] 토큰이 요청에 포함되지 않았습니다.");
            setFail(resultDto);
        }

        String info = jwtProvider.getUsername(token);
        if (info == null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setDetailMessage("유효하지 않은 토큰입니다.");
            log.warn("[createTeam] 유효하지 않은 토큰입니다.");
            setFail(resultDto);
     }
        Member member = memberRepository.getByEmail(info);
        ResultDto resultDto = new ResultDto();
        if(member != null){
            Team team = saveTeamInfo(createTeamRquestDto,image);
            team.setMember(member);
            teamDao.save(team);
            addMemberRoleManager(member,team);
            member.setTeam(team);
            memberDao.save(member);

            resultDto.setDetailMessage("팀 생성 완료.");
            setSuccess(resultDto);
        }else{
            resultDto.setDetailMessage("팀 생성 실패.");
            setFail(resultDto);
        }

        return resultDto;
    }

    @Override
    public TeamResponseDto getTeam(HttpServletRequest servletRequest, Long teamId) {
        Team team = teamDao.findByTeamId(teamId);
        TeamResponseDto teamResponseDto = new TeamResponseDto();
        teamResponseDto.setTeamName(team.getTeamName());
        teamResponseDto.setTeamImageUrl(team.getTeamImageUrl());
        teamResponseDto.setTeam_introduce(team.getTeam_introduce());
        teamResponseDto.setRegion(team.getRegion());
        teamResponseDto.setTown(team.getTown());
        teamResponseDto.setMatchLocation(team.getMatchLocation());
        teamResponseDto.setDues(team.getDues());
        teamResponseDto.setTeamGender(teamResponseDto.getTeamGender());
        teamResponseDto.setAgeRange(team.getAgeRange());
        teamResponseDto.setTeamLevel(team.getTeamLevel());
        teamResponseDto.setActivityDays(team.getActivityDays());
        teamResponseDto.setActivityTime(team.getActivityTime());
        teamResponseDto.setPositionRequired(team.getPositionRequired());
        return teamResponseDto;
    }

    @Override
    public Team saveTeamInfo(CreateTeamRquestDto createTeamRquestDto ,MultipartFile image) throws IOException {
        String imageUrl = saveImageS3(image);

        return Team.builder()
                .teamName(createTeamRquestDto.getTeamName())
                .teamImageUrl(imageUrl)
                .region(createTeamRquestDto.getRegion())
                .town(createTeamRquestDto.getTown())
                .matchLocation(createTeamRquestDto.getMatchLocation())
                .activityDays(createTeamRquestDto.getActivityDays())
                .activityTime(createTeamRquestDto.getActivityTime())
                .teamGender(createTeamRquestDto.getTeamGender())
                .ageRange(createTeamRquestDto.getAgeRange())
                .dues(createTeamRquestDto.getDues())
                .teamLevel(createTeamRquestDto.getTeamLevel())
                .positionRequired(createTeamRquestDto.getPositionRequired())
                .team_introduce(createTeamRquestDto.getTeam_introduce())
                .inviteCode(make_InviteCode())
                .create_At(LocalDateTime.now())
                .update_At(LocalDateTime.now())
                .build();
    }

    public  String saveImageS3(MultipartFile profileImage) throws IOException {
        return s3Uploader.uploadImage(profileImage,"image");
    }

    private String make_InviteCode(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();

        String randomNum = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return randomNum;
    }

    private void addMemberRoleManager(Member member, Team team){
        MemberRole memberRole = new MemberRole();
        memberRole.setMember(member);
        memberRole.setTeam(team);
        memberRole.setRole("Role_Manager");
        member.getMemberRoles().add(memberRole);
        memberRoleDao.saveMemberRole(memberRole);
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
    }
}
