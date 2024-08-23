package com.springboot.yogijogii.data.repository.member.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogii.data.repository.member.MemberRepositoryCustom;
import com.springboot.yogijogii.entity.QMember;
import com.springboot.yogijogii.entity.QTeam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

   private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<String> selectTeamMember(Long teamId) {
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        List<String> teamMembers = jpaQueryFactory
                .select(qMember.name)
                .from(qMember)
                .innerJoin(qMember.team(), qTeam)
                .where(qTeam.teamId.eq(teamId))
                .fetch();

        return teamMembers;
    }

}
