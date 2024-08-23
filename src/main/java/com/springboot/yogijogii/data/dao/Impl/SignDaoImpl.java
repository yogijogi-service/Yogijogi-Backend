package com.springboot.yogijogii.data.dao.Impl;


import com.springboot.yogijogii.data.dao.SignDao;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignDaoImpl implements SignDao {
    private final MemberRepository memberRepository;
    @Override
    public void saveSignUpInfo(Member member) {
        memberRepository.save(member);
    }
}
