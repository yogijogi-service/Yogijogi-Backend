package com.springboot.yogijogii.data.dao;


import com.springboot.yogijogii.data.entity.Member;

public interface AuthDao {

    Member KakaoMemberSave(Member member);
    Member kakaoUserFind(String email);
}
