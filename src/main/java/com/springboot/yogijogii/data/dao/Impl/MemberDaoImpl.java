package com.springboot.yogijogii.data.dao.Impl;

import com.springboot.yogijogii.data.dao.MemberDao;
import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDaoImpl implements MemberDao {
    private final MemberRepository memberRepository;

    @Override
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public void save(Member member) { memberRepository.save(member);
    }
}
