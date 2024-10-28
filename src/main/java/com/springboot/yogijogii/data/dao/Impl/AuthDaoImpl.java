package com.springboot.yogijogii.data.dao.Impl;


import com.springboot.yogijogii.data.dao.AuthDao;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDaoImpl implements AuthDao {
    private final MemberRepository memberRepository;

    @Override
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member findMember(String email) {
        return memberRepository.findByEmail(email);
    }


}
