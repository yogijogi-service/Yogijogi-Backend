package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.MemberRoleDao;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberRole;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.data.entity.Team;
import com.springboot.yogijogii.data.repository.member.ServiceRoleRepository;
import com.springboot.yogijogii.data.repository.memberRole.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberRoleDaoImpl implements MemberRoleDao {
    private final MemberRoleRepository memberRoleRepository;
    private final ServiceRoleRepository serviceRoleRepository;

    @Override
    public void saveMemberRole(MemberRole memberRole) {
        memberRoleRepository.save(memberRole);
    }

    @Override
    public void saveServiceRole(ServiceRole serviceRole) {
        serviceRoleRepository.save(serviceRole);
    }

    @Override
    public boolean existsByMemberAndTeamAndRole(Member member, Team team, String role) {
        return memberRoleRepository.existsByMemberAndTeamAndRole(member, team, role);
    }
    @Override
    public boolean existsByMemberAndTeam(Member member, Team team) {
        return memberRoleRepository.existsByMemberAndTeam(member, team);
    }

}
