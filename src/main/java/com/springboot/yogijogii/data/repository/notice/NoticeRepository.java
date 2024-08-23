package com.springboot.yogijogii.data.repository.notice;


import com.springboot.yogijogii.data.entity.Notice;
import com.springboot.yogijogii.data.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
    // 특정 팀에 속한 공지사항을 조회하는 메서드
    List<Notice> findByTeam(Team team);


}
