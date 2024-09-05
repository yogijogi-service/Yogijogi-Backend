package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.entity.Member;

import java.util.Optional;

public interface MemberDao {
    Member findMemberByEmail(String email);

    Optional<Member> findMemberByID(Long memberId);

    void save(Member member);
}
