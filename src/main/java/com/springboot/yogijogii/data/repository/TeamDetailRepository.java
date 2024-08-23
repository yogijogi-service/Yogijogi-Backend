package com.springboot.yogijogii.data.repository;


import com.springboot.yogijogii.data.entity.TeamDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDetailRepository extends JpaRepository<TeamDetail,Long> {
//    Optional<TeamDetail> findByTeam_TeamId(Long teamId);
}
