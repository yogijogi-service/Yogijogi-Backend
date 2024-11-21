package com.springboot.yogijogii.service.Impl;


import com.springboot.yogijogii.data.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private  final EntityManager em;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("[loadUserByPhoneNum] : {} ", email);

        TypedQuery<Member> query = em.createQuery(
                "SELECT m FROM Member m LEFT JOIN FETCH m.teamMembers WHERE m.email = :email",
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
