package com.springboot.yogijogii.data.dao;


import com.springboot.yogijogii.data.entity.Member;

public interface SignDao {
    void saveSignUpInfo(Member member);
}
