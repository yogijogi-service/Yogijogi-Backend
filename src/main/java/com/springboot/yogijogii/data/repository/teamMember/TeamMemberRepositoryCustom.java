package com.springboot.yogijogii.data.repository.teamMember;

import com.springboot.yogijogii.data.entity.TeamMember;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepositoryCustom {
    Optional<List<TeamMember>> getTeamMemberByUserIdAndPosition(Long userId, String position);
    boolean isTeamMemberAndManager(Long userId, Long teamId);

}
