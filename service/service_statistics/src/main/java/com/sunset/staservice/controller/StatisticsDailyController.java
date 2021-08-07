package com.sunset.staservice.controller;


import com.sunset.commonutils.Result;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import com.sunset.staservice.client.UcenterClient;
import com.sunset.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-08-04
 */
@RestController
@RequestMapping("/staservice/sta")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService dailyService;
//    远程调用ucenter,统计某一天注册人数
    @PostMapping("/countRegister/{day}")
    public Result countRegister(@PathVariable String day){
        dailyService.countRegister(day);
        return Result.ok();
    }

//    图表显示，返回两部分数据，一部分是日期的json数组，一部分是数量的json数组
    @GetMapping("/showData/{type}/{begin}/{end}")
    public Result showData(@PathVariable String type,@PathVariable String begin,@PathVariable String end){
        System.out.println(type);
        Map<String,Object> map = dailyService.getShowData(type,begin,end);
        return Result.ok().data("map",map);

    }

}

