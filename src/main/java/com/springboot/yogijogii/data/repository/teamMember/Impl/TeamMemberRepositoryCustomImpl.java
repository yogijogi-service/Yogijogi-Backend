package com.springboot.yogijogii.data.repository.teamMember.Impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogii.data.entity.QTeamMember;
import com.springboot.yogijogii.data.entity.TeamMember;
import com.springboot.yogijogii.data.repository.teamMember.TeamMemberRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamMemberRepositoryCustomImpl implements TeamMemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QTeamMember qTeamMember =QTeamMember.teamMember;

    @Override
    public Optional<List<TeamMember>> getTeamMemberByUserIdAndPosition(Long teamId, String position) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qTeamMember)
                .where(qTeamMember.team().teamId.eq(teamId)
                        .and(qTeamMember.position.eq(position)))
                .fetch());
    }

    @Override
    public boolean isTeamMemberAndManager(Long userId, Long teamId) {
        QTeamMember qTeamMember = QTeamMember.teamMember;

        // QueryDSL을 이용한 조건 쿼리
        BooleanExpression isMemberInTeam = qTeamMember.member().memberId.eq(userId)
                .and(qTeamMember.team().teamId.eq(teamId))
                .and(qTeamMember.role.in("ROLE_MANAGER", "ROLE_SUPER_MANAGER"));

        // 해당 조건을 만족하는 데이터가 존재하는지 확인
        return jpaQueryFactory.selectOne()
                .from(qTeamMember)
                .where(isMemberInTeam)
                .fetchFirst() != null; // 데이터가 있으면 true 반환
    }
}
