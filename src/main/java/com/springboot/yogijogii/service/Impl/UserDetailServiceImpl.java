package com.springboot.yogijogii.service.Impl;


import com.springboot.yogijogii.data.entity.Member;
import com.springboot.yogijogii.data.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("[loadUserByPhoneNum] : {} ", email);

        TypedQuery<Member> query = em.createQuery(
                "SELECT m FROM Member m LEFT JOIN FETCH m.memberRoles WHERE m.email = :email",
                Member.class);
        query.setParameter("email", email);

        Member member = query.getSingleResult();

        if (member == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + email);
        }
        member.getMemberRolesWithInit();
        return member;
    }
}
