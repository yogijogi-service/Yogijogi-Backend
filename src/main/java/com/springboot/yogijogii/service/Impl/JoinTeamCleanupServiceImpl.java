package com.springboot.yogijogii.service.Impl;

import com.springboot.yogijogii.data.dao.JoinTeamDao;
import com.springboot.yogijogii.data.entity.JoinTeam;
import com.springboot.yogijogii.service.JoinTeamCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinTeamCleanupServiceImpl implements JoinTeamCleanupService {
    private final JoinTeamDao joinTeamDao;

    @Scheduled(cron = "0 * * * * ?")
    public void cleanUpJoinRequests() {
        try {
            // 현재 시간으로부터 7일 뒤에 삭제
        LocalDateTime onWeekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);

        List<JoinTeam> expiredRequests = joinTeamDao.findByStatusAndDate("ACCEPT", onWeekAgo);
        expiredRequests.addAll(joinTeamDao.findByStatusAndDate("REJECT", onWeekAgo));

            if (!expiredRequests.isEmpty()) {
                joinTeamDao.deleteAll(expiredRequests);
                log.info("Expired join requests deleted: {}", expiredRequests.size());
            } else {
                log.info("No expired join requests to delete.");
            }
        } catch (Exception e) {
            log.error("Error during join request cleanup: ", e);
        }
    }
}
