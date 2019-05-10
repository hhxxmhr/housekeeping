package com.mhr.housekeeping.controller.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class UserUpgradeTask {
    @Scheduled(cron = "0 0 8 * * ?")
    public void updateGrade(){

    }
}
