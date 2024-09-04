package com.springboot.yogijogii.data.repository.team.Impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.springboot.yogijogii.data.dto.teamDto.search.SearchTeamFilterRequestDto;
import com.springboot.yogijogii.data.entity.QTeam;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.team.TeamRepositoryCustom;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QTeam team = QTeam.team;

    @Override
    public List<Team> searchTeams(SearchTeamFilterRequestDto searchTeamFilterRequestDto) {
        return jpaQueryFactory
                .selectFrom(team)
                .where(
                        eqRegion(searchTeamFilterRequestDto.getTeamRegion()),
                        eqLevel(searchTeamFilterRequestDto.getTeamLevel()),
                        eqGender(searchTeamFilterRequestDto.getTeamGender()),
                        eqAgeRange(searchTeamFilterRequestDto.getAgeRange()),
                        eqActivityTime(searchTeamFilterRequestDto.getActivityTime()),
                        eqActivityDays(searchTeamFilterRequestDto.getActivityDays())
                )
                .fetch();
    }

    private BooleanExpression eqRegion(List<String> regions) {
        if (CollectionUtils.isEmpty(regions)) {
            return null;
        }
        return team.region.in(regions);
    }

    private BooleanExpression eqGender(List<String> genders) {
        if (CollectionUtils.isEmpty(genders)) {
            return null;
        }
        return team.teamGender.in(genders);
    }

    private BooleanExpression eqLevel(List<String> levels) {
        if (CollectionUtils.isEmpty(levels)) {
            return null;
        }
        return team.teamLevel.in(levels);
    }

    private BooleanExpression eqAgeRange(List<String> ageRanges) {
        if (CollectionUtils.isEmpty(ageRanges)) {
            return null;
        }
        return team.teamLevel.in(ageRanges);
    }

    private BooleanExpression eqActivityTime(List<String> activityTimes) {
        if (CollectionUtils.isEmpty(activityTimes)) {
            return null;
        }
        return team.activityTime.any().in(activityTimes);
    }

    private BooleanExpression eqActivityDays(List<String> activityDays) {
        if (CollectionUtils.isEmpty(activityDays)) {
            return null;
        }
        return team.activityDays.any().in(activityDays); // 다중 값 지원
    }
}