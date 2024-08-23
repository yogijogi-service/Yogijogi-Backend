package com.springboot.yogijogii.data.repository;


import com.springboot.yogijogii.data.entity.MemberRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoleRepository extends JpaRepository<MemberRole,Long> {
    @EntityGraph(attributePaths = {"member", "team", "role"})
    Optional<MemberRole> findById(Long id);
}
