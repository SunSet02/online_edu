package com.sunset.staservice.client;

import com.sunset.commonutils.Result;
import com.sunset.staservice.client.impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {
    //    查询某一天的注册人数
    @GetMapping("/educenter/member/countRegieter/{day}")
    public Result countRegieter(@PathVariable("day") String day);
}
