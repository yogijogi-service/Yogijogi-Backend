package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.LeaveTeamDao;
import com.springboot.yogijogii.data.entity.LeaveTeam;
import com.springboot.yogijogii.data.repository.leaveTeam.LeaveTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveTeamDaoImpl implements LeaveTeamDao {
    public final LeaveTeamRepository leaveTeamRepository;


    @Override
    public void save(LeaveTeam leaveTeam) {
        leaveTeamRepository.save(leaveTeam);
    }
}
