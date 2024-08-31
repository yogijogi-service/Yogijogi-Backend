package com.springboot.yogijogii.data.dao.Impl;


import com.springboot.yogijogii.data.dao.SignDao;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.entity.MemberAgreement;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import com.springboot.yogijogii.data.repository.member.MemverAgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignDaoImpl implements SignDao {
    private final MemberRepository memberRepository;
    private final MemverAgreementRepository memverAgreementRepository;
    @Override
    public void saveSignUpInfo(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void saveMemberAgree(MemberAgreement memberAgreement) {
        memverAgreementRepository.save(memberAgreement);
    }
}
