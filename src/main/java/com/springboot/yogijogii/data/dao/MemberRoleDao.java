package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberRole;
import com.springboot.yogijogii.data.entity.ServiceRole;
import com.springboot.yogijogii.data.entity.Team;

public interface MemberRoleDao {
    void saveMemberRole(MemberRole memberRole);
    void saveServiceRole(ServiceRole serviceRole);
    boolean existsByMemberAndTeamAndRole(Member member, Team team, String role);

    boolean existsByMemberAndTeam(Member member, Team team);
}
