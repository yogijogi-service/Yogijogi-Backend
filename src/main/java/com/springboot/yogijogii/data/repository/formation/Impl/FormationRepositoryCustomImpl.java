package com.springboot.yogijogii.data.repository.formation.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogii.data.entity.Formation;
import com.springboot.yogijogii.data.entity.QFormation;
import com.springboot.yogijogii.data.entity.QTeam;
import com.springboot.yogijogii.data.repository.formation.FormationRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FormationRepositoryCustomImpl implements FormationRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    QFormation qFormation = QFormation.formation;

    @Override
    public Optional<Formation> findByName(Long teamId, String name) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qFormation)
                .where(qFormation.team().teamId.eq(teamId).and(qFormation.name.eq(name)))
                .fetchOne());
    }
}
