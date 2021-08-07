package com.sunset.eduService.client;

import com.sunset.commonutils.Result;
import com.sunset.eduService.client.impl.VodClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name="service-vod",fallback = VodClientImpl.class)//加注解，里面写服务名称
public interface VodClient {
//    定义要调用方法的完全路径，注意，@PathVariable一定要指定名称
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public Result removeAlyVideo(@PathVariable("id") String id);

    //    删除多个视频
    @DeleteMapping("/eduvod/video/delete-batch")
    public Result deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
