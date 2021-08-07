package com.sunset.eduOrder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-edu")
public interface EduClient {
    @GetMapping("/eduService/coursefront/getCourseInfo/{courseId}")
    public com.sunset.commonutils.ordervo.CourseWebVo getCourseInfo(@PathVariable("courseId") String courseId);
}
