package com.springboot.yogijogii.data.dao;

import com.springboot.yogijogii.data.entity.Member;

public interface MemberDao {
    Member findMemberByEmail(String email);

    void save(Member member);
}
