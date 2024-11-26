package com.springboot.yogijogii.data.repository.teamStrategy.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogii.data.entity.QTeamStrategy;
import com.springboot.yogijogii.data.entity.TeamStrategy;
import com.springboot.yogijogii.data.repository.teamStrategy.TeamStrategyRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamStrategyRepositoryCustomImpl implements TeamStrategyRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QTeamStrategy qTeamStrategy = QTeamStrategy.teamStrategy;

    @Override
    public Optional<List<TeamStrategy>> findTeamStrategyByDate(Long teamId, LocalDate date) {

        return Optional.ofNullable(jpaQueryFactory.selectFrom(qTeamStrategy)
                .where(qTeamStrategy.team().teamId.eq(teamId)
                        .and(qTeamStrategy.matchDay.eq(date)))
                .fetch());
    }

    @Override
    public Optional<List<TeamStrategy>> findAllTeamStrategyByDateRange(Long teamId, LocalDate startOfMonth, LocalDate endOfMonth) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qTeamStrategy)
                .where(qTeamStrategy.team().teamId.eq(teamId)
                        .and(qTeamStrategy.matchDay.between(startOfMonth, endOfMonth))) // 월 범위 조건
                .fetch());
    }
}
