package com.springboot.yogijogii.data.repository.team.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.team.TeamRepositoryCustom;
import com.springboot.yogijogii.entity.QTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Team> searchTeam(String team_name) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.teamName.like("%"+team_name+"%"))
                .fetch();
    }

    @Override
    public List<Team> searchRegion(String region) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.region.like("%"+region+"%"))
                .fetch();
    }

    @Override
    public List<Team> searchGender(String teamGender) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.teamGender.like("%"+teamGender+"%"))
                .fetch();
    }
    @Override
    public List<Team> searchDay(String activityDays) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.activityDays.contains(activityDays))
                .fetch();
    }
    @Override
    public List<Team> searchTime(String activityTime) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.activityTime.contains(activityTime))
                .fetch();
    }



}
