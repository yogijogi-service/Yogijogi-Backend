package com.springboot.yogijogii.data.repository.member;


import com.springboot.yogijogii.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
    Member findByEmail(String email);
    Member getByEmail(String email);




}
