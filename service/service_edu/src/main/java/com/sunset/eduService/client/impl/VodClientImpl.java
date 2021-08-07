package com.sunset.eduService.client.impl;

import com.sunset.commonutils.Result;
import com.sunset.eduService.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
//服务出错会执行实现类的方法
public class VodClientImpl implements VodClient {
    @Override
    public Result removeAlyVideo(String id) {
        return Result.error().message("time out");
    }

    @Override
    public Result deleteBatch(List<String> videoIdList) {
        return Result.error().message("time out");
    }
}
