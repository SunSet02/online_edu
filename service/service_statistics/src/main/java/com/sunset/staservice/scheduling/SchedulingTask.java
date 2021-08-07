package com.sunset.staservice.scheduling;

import com.sunset.staservice.service.StatisticsDailyService;
import com.sunset.staservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SchedulingTask {
    @Autowired
    private StatisticsDailyService dailyService;
//    在每天凌晨1点执,把前一天数据进行添加行方法
@Scheduled(cron = "0 0 1 * * ? ")//每隔5秒钟执行方法
    public void task(){
        dailyService.countRegister(DateUtils.formatDate(DateUtils.addDays(new Date(),-1)));
    }

//    @Scheduled(cron = "0/5 * * * * ?")//每隔5秒钟执行方法
//    public void task1(){
//        System.out.println("task1执行了+++++++++++++++++++");
//    }
}

