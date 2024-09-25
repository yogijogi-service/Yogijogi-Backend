package com.springboot.yogijogii.data.dao;


import com.springboot.yogijogii.data.entity.Member;

public interface AuthDao {

    Member saveMember(Member member);
    Member findMember(String email);
}
