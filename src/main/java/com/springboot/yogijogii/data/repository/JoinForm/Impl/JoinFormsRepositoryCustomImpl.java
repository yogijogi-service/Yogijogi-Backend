package com.springboot.yogijogii.data.repository.JoinForm.Impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogii.data.entity.JoinForms;
import com.springboot.yogijogii.entity.QJoinForms;
import com.springboot.yogijogii.entity.QMember;
import com.springboot.yogijogii.entity.QTeam;
import com.springboot.yogijogii.data.repository.JoinForm.JoinFormsCustom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class JoinFormsRepositoryCustomImpl implements JoinFormsCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<JoinForms> findPendingJoinFormsByTeam_TeamId(Long teamId) {
        QJoinForms qJoinForm = QJoinForms.joinForms;
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        return jpaQueryFactory
                .select(
                        Projections.fields(JoinForms.class,
                                qMember.name.as("memberName"),
                                qMember.position.as("memberPosition"),
                                qJoinForm.joinStatus,
                                qJoinForm.additionalInfo
                        ))
                .from(qJoinForm)
                .leftJoin(qJoinForm.member(), qMember)
                .leftJoin(qJoinForm.team(), qTeam)
                .where(qTeam.teamId.eq(teamId)
                        .and(qJoinForm.joinStatus.eq("대기중")))
                .fetch();
    }
}
