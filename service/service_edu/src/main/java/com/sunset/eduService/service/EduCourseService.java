package com.sunset.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.eduService.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunset.eduService.entity.frontvo.CourseVo;
import com.sunset.eduService.entity.frontvo.CourseWebVo;
import com.sunset.eduService.entity.vo.CourseInfoVo;
import com.sunset.eduService.entity.vo.CoursePublishVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo getPublishCourseInfo(String courseId);

    void removeCourse(String courseId);


    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseVo courseVo);

    CourseWebVo getBaseCourseInfo(String courseId);
}
