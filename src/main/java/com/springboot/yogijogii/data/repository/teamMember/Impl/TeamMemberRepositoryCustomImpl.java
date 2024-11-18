package com.springboot.yogijogii.data.repository.teamMember.Impl;

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
    public Optional<List<TeamMember>> getTeamMemberByUserIdAndPosition(Long userId, String position) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qTeamMember)
                .where(qTeamMember.member().memberId.eq(userId)
                        .and(qTeamMember.position.eq(position)))
                .fetch());
    }
}
