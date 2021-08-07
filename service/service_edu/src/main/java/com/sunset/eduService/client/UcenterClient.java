package com.sunset.eduService.client;


import com.sunset.commonutils.Result;
import com.sunset.commonutils.vo.CommentUcenterVo;
import com.sunset.eduService.client.impl.UcenterClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name="service-ucenter",fallback = UcenterClientImpl.class)//加注解，里面写服务名称
public interface UcenterClient {
    @GetMapping("/educenter/member/getCommentInfo/{id}")
    public CommentUcenterVo getCommentUcenterInfo(@PathVariable String id);
}
