package com.sunset.eduOrder.client;

import com.sunset.commonutils.ordervo.UcenterMemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    @GetMapping("/educenter/member/getOrderUserInfo/{id}")
    public UcenterMemberVo getOrderUserInfo(@PathVariable("id") String id);
}
