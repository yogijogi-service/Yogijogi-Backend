package com.springboot.yogijogii.data.repository.member;



import java.util.List;

public interface MemberRepositoryCustom {
    List<String> selectTeamMember(Long teamId);

}
