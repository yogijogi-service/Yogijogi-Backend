package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.result.ResultStatusService;
import com.springboot.yogijogii.s3.S3Uploader;
import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.dao.TeamMemberDao;
import com.springboot.yogijogii.data.dao.TeamDao;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.teamDto.CreateTeamRquestDto;
import com.springboot.yogijogii.data.dto.teamDto.TeamMemberListDto;
import com.springboot.yogijogii.data.dto.teamDto.TeamResponseDto;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.jwt.JwtProvider;
import com.springboot.yogijogii.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {

    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;
    private final JwtProvider jwtProvider;
    private final TeamDao teamDao;
    private final MemberDao memberDao;
    private final TeamMemberDao teamMemberDao;
    private final ResultStatusService resultStatusService;

    @Override
    public ResultDto createTeam(CreateTeamRquestDto createTeamRquestDto, MultipartFile image,HttpServletRequest request) throws IOException{
        String token = jwtProvider.resolveToken(request);
        if (token == null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setDetailMessage("토큰이 요청에 포함되지 않았습니다.");
            log.warn("[createTeam] 토큰이 요청에 포함되지 않았습니다.");
            resultStatusService.setFail(resultDto);
        }

        String info = jwtProvider.getUsername(token);
        if (info == null) {
            ResultDto resultDto = new ResultDto();
            resultDto.setDetailMessage("유효하지 않은 토큰입니다.");
            log.warn("[createTeam] 유효하지 않은 토큰입니다.");
            resultStatusService.setFail(resultDto);
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
            resultStatusService.setSuccess(resultDto);
        }else{
            resultDto.setDetailMessage("팀 생성 실패.");
            resultStatusService.setFail(resultDto);
        }
        return resultDto;
    }

    @Override
    public TeamResponseDto getTeam(HttpServletRequest servletRequest, Long teamId) {
        Team team = teamDao.findByTeamId(teamId);
        TeamResponseDto teamResponseDto = TeamResponseDto.builder()
                .teamName(team.getTeamName())
                .teamImageUrl(team.getTeamImageUrl())
                .inviteCode(team.getInviteCode())
                .team_introduce(team.getTeam_introduce())
                .region(team.getRegion())
                .town(team.getTown())
                .matchLocation(team.getMatchLocation())
                .dues(team.getDues())
                .memberCount(team.getTeamMembers().stream().count())
                .teamGender(team.getTeamGender())
                .ageRange(team.getAgeRange())
                .teamLevel(team.getTeamLevel())
                .activityDays(team.getActivityDays())
                .activityTime(team.getActivityTime())
                .positionRequired(team.getPositionRequired())
                .build();
        return teamResponseDto;
    }

    @Override
    public List<TeamMemberListDto> getTeamMemberList(HttpServletRequest servletRequest, Long teamId, String position, String sort) {
        Team team = teamDao.findByTeamId(teamId);

        // 정렬 기준 설정 (기본값: createdDate 오름차순)
        Sort sortOrder = "최신 가입순".equals(sort) ? Sort.by(Sort.Direction.DESC, "createdDate") : Sort.by(Sort.Direction.ASC, "createdDate");

        // position에 따른 멤버 조회
        List<TeamMember> teamMembers;
        if ("전체".equals(position)) {
            teamMembers = teamMemberDao.findByTeam(team, sortOrder);
        } else {
            teamMembers = teamMemberDao.findByTeamAndPosition(team, position, sortOrder);
        }

        return teamMembers.stream()
                .map(teamMember -> TeamMemberListDto.builder()
                        .id(teamMember.getId())
                        .profileUrl(teamMember.getMember().getProfileUrl())
                        .name(teamMember.getMember().getName())
                        .position(teamMember.getPosition())
                        .role(teamMember.getRole())
                        .build())
                .collect(Collectors.toList());
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
        TeamMember teamMember = new TeamMember();
        teamMember.setMember(member);
        teamMember.setTeam(team);
        teamMember.setRole("ROLE_MANAGER");
        teamMember.setTeamColor("#000000");
        teamMember.setFavoriteTeam(false);
        member.getTeamMembers().add(teamMember);
        teamMemberDao.saveTeamMember(teamMember);
    }
}
