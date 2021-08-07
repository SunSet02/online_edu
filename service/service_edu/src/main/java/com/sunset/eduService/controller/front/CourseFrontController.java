package com.sunset.eduService.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.commonutils.JwtUtils;
import com.sunset.commonutils.Result;
import com.sunset.eduService.client.OrderClient;
import com.sunset.eduService.entity.EduCourse;
import com.sunset.eduService.entity.chapter.ChapterVo;
import com.sunset.eduService.entity.frontvo.CourseVo;
import com.sunset.eduService.entity.frontvo.CourseWebVo;
import com.sunset.eduService.service.EduChapterService;
import com.sunset.eduService.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduService/coursefront")
//@CrossOrigin
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private OrderClient orderClient;
//    条件查询带分页
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public Result getFrontCourseList(@PathVariable long page, @PathVariable long limit, @RequestBody(required = false) CourseVo courseVo){
        Page<EduCourse> pageCourse = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(pageCourse,courseVo);
        return Result.ok().data("map",map);
    }

//    课程详情方法
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public Result getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
//        根据课程id查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
//        根据课程id查询章节小节信息
        List<ChapterVo> chapterVideoByList = chapterService.getChapterVideoById(courseId);
//        根据课程id和用户id查询当前课程是否已经被购买
//        jwt工具类得到用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
//            如果用户未登录则返回错误，然后前端有个拦截器可以拦截响应，跳转到登录页面
            return Result.error().code(28004).message("请先登录");
        }

        boolean b = orderClient.isBuyCourse(courseId, memberId);
        return Result.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoByList).data("isBuy",b);
    }
//    根据课程id，查询课程信息
    @GetMapping("/getCourseInfo/{courseId}")
    public com.sunset.commonutils.ordervo.CourseWebVo getCourseInfo(@PathVariable String courseId) {
        CourseWebVo webVo = courseService.getBaseCourseInfo(courseId);
        com.sunset.commonutils.ordervo.CourseWebVo courseWebVo = new com.sunset.commonutils.ordervo.CourseWebVo();
        BeanUtils.copyProperties(webVo, courseWebVo);
        return courseWebVo;
    }
}
