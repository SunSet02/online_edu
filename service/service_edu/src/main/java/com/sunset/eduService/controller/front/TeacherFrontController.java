package com.sunset.eduService.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.commonutils.Result;
import com.sunset.eduService.entity.EduCourse;
import com.sunset.eduService.entity.EduTeacher;
import com.sunset.eduService.service.EduCourseService;
import com.sunset.eduService.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduService/teacherfront")
//@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;
//    分页查询讲师模块
    @PostMapping("/getTeacherFrontList/{page}/{limit}")
    public Result getTeacherFrontList(@PathVariable Long page,@PathVariable long limit){
        Page<EduTeacher> pageTeacher = new Page<>(page,limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(pageTeacher);
//        返回分页所有数据，自己写底层
        return Result.ok().data("map",map);
    }

//    讲师详情的功能
    @GetMapping("/getTeacherFrontInfo/{teacherId}")
    public Result getTeacherFrontInfo(@PathVariable String teacherId){
//        查询讲师基本信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
//        查询讲师课程信息
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id",teacherId);
        List<EduCourse> eduCourseList = courseService.list(queryWrapper);
        return Result.ok().data("teacher",eduTeacher).data("courseList",eduCourseList);
    }
}
