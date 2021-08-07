package com.sunset.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.commonutils.Result;
import com.sunset.eduService.entity.EduCourse;
import com.sunset.eduService.entity.EduTeacher;
import com.sunset.eduService.entity.vo.CourseInfoVo;
import com.sunset.eduService.entity.vo.CoursePublishVo;
import com.sunset.eduService.entity.vo.CourseQuery;
import com.sunset.eduService.entity.vo.TeacherQuery;
import com.sunset.eduService.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
@RestController
@RequestMapping("/eduService/course")
//@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;

//    删除课程
    @DeleteMapping("/{courseId}")
    public Result deleteCourse(@PathVariable String courseId){
        eduCourseService.removeCourse(courseId);
        return Result.ok();
    }
//分页带条件查询
    @PostMapping("/pageCourseCondition/{current}/{limit}")
    public Result pageCourseCondition(@PathVariable("current") Long current, @PathVariable("limit")Long limit, @RequestBody(required = false) CourseQuery courseQuery){
        Page<EduCourse> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        //多条件组合查询
        String title = courseQuery.getTitle();
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();
        //判断值是否为空，不为空就做拼接
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        if (!StringUtils.isEmpty(teacherId) ) {
            queryWrapper.eq("teacher_id", teacherId);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.ge("subject_parent_id", subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.ge("subject_id", subjectId);
        }
        queryWrapper.orderByDesc("gmt_create");
        eduCourseService.page(page, queryWrapper);
        long total = page.getTotal();//得到总记录数
        List<EduCourse> records = page.getRecords();//得到每页的list集合
        return Result.ok().data("total",total).data("rows",records);
    }
    @PostMapping("/addCourseInfo")
    public Result addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return Result.ok().data("courseId",id);
    }

    //根据课程id查询课程基本信息
    @GetMapping("/getCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId){
       CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
       return Result.ok().data("courseInfoVo",courseInfoVo);
    }


//    修改课程信息
    @PostMapping("/updateCourseInfo")
    public Result updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return Result.ok();
    }


//    根据课程id查询多表查询信息
    @GetMapping("/getPublishCourseInfo/{courseId}")
    public Result getPublishCourseInfo(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = eduCourseService.getPublishCourseInfo(courseId);
        return Result.ok().data("coursePublishInfo",coursePublishVo);
    }

//    课程最终发布，修改课程状态
    @PostMapping("/publishCourse/{id}")
    public Result publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return Result.ok();
    }



}

