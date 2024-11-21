package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.FormationDao;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationDetailResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.response.FormationResponseDto;
import com.springboot.yogijogii.data.dto.fomationDto.request.Formation_detailRequestDto;
import com.springboot.yogijogii.data.entity.Formation;
import com.springboot.yogijogii.data.entity.FormationDetail;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.repository.formation.FormationDetailRepository;
import com.springboot.yogijogii.data.repository.formation.FormationRepository;
import com.springboot.yogijogii.data.repository.teamMember.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormationDaoImpl implements FormationDao {

    private final TeamMemberRepository teamMemberRepository;
    private final FormationRepository formationRepository;
    private final FormationDetailRepository formationDetailRepository;


    @Override
    public void saveFormation(String formationName, List<Formation_detailRequestDto> formationDetailRequestDtos) {
        Formation formation = Formation.builder()
                .name(formationName)
                .build();

        for (Formation_detailRequestDto formationDetailRequestDto : formationDetailRequestDtos) {
            Long playerId = formationDetailRequestDto.getPlayerId();
            TeamMember teamMember = teamMemberRepository.findById(playerId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid player ID: " + playerId));

            // FormationDetail 생성
            FormationDetail formationDetail = FormationDetail.builder()
                    .x(formationDetailRequestDto.getX())
                    .y(formationDetailRequestDto.getY())
                    .color(teamMember.getTeamColor())
                    .detailPosition(teamMember.getPosition())
                    .teamMember(teamMember)
                    .formation(formation) // Formation 매핑
                    .build();

            formation.addFormationDetail(formationDetail); // 연관 관계 추가
        }

        formationRepository.save(formation); // Formation 저장
    }

    // 여기 DAO는 복잡해서 주석 처리 해놓은 거임. 추후에 지울 예정. - kys
    @Override
    public FormationResponseDto getFormation(Long formationId) {
        //포메이션 불러오기
        Optional<Formation> formation = formationRepository.findById(formationId);

        // 포메이션 아이디로 포메이션 디테일 불러오기
        List<FormationDetail> formationDetailList = formationDetailRepository.findByFormationId(formationId);

        //Dto에 데이터 넣기.
        List<FormationDetailResponseDto> formationDetailResponseDtoList = new ArrayList<>();
        for(FormationDetail formationDetail : formationDetailList){
            FormationDetailResponseDto formationDetailResponseDto = new FormationDetailResponseDto(
                    formationDetail.getId(),
                    formationDetail.getTeamMember().getMember().getName(),
                    formationDetail.getDetailPosition(),
                    formationDetail.getX(),
                    formationDetail.getY()
            );
            formationDetailResponseDtoList.add(formationDetailResponseDto);
        }
        FormationResponseDto formationResponseDto = new FormationResponseDto(
                formationId,
                formation.get().getName(),
                formationDetailResponseDtoList
        );

        return formationResponseDto;
    }

    @Override
    public Formation findById(Long formationId) {
        return formationRepository.findById(formationId).get();
    }
}
