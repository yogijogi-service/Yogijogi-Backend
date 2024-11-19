package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.Result.ResultStatusService;
import com.springboot.yogijogii.data.dao.FormationDao;
import com.springboot.yogijogii.data.dto.ResultDto;
import com.springboot.yogijogii.data.dto.fomationDto.FormationRequestDto;
import com.springboot.yogijogii.data.dto.fomationDto.Formation_detailRequestDto;
import com.springboot.yogijogii.data.entity.Formation;
import com.springboot.yogijogii.data.entity.FormationDetail;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.repository.formation.FormationDetailRepository;
import com.springboot.yogijogii.data.repository.formation.FormationRespotiroy;
import com.springboot.yogijogii.data.repository.teamMember.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormationDaoImpl implements FormationDao {

    private final TeamMemberRepository teamMemberRepository;
    private final FormationRespotiroy formationRespotiroy;


    @Override
    public void saveFormation(String formationName,List<Formation_detailRequestDto> formationDetailRequestDtos) {
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
                    .formation(formation)
                    .build();

            log.info("[FormationDetail] Created: {}", formationDetail.getFormation());
            formation.addFormationDetail(formationDetail);
        }
        formationRespotiroy.save(formation);
    }
}
