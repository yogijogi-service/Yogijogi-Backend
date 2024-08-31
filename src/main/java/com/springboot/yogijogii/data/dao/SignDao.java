package com.springboot.yogijogii.data.dao;


import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberAgreement;

public interface SignDao {
    void saveSignUpInfo(Member member);
    void saveMemberAgree(MemberAgreement memberAgreement);
}
