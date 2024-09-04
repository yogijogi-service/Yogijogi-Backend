package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.data.repository.joinTeam.JoinTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinTeamDaoImpl implements JoinTeamDao {
    private final JoinTeamRepository joinTeamRepository;

    @Override
    public void save(JoinTeam joinTeam) {
        joinTeamRepository.save(joinTeam);
    }
}
