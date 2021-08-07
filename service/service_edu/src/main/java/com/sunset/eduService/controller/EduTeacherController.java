package com.sunset.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.commonutils.Result;
import com.sunset.eduService.entity.EduTeacher;
import com.sunset.eduService.entity.vo.TeacherQuery;
import com.sunset.eduService.service.EduTeacherService;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/eduService/teacher")
//@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;
    //    查询数据
    @GetMapping("/findAll")
    public Result findAll(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return Result.ok().data("items",list);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteTeacherById(@PathVariable("id") String id){
        boolean b = eduTeacherService.removeById(id);
        return b? Result.ok(): Result.error();
    }
    //分页查询
    @GetMapping("/pageTeacher/{current}/{limit}")
    public Result pageListRTeacher(@PathVariable("current") Long current,@PathVariable("limit")Long limit){
        Page<EduTeacher> page = new Page<>(current,limit);
        //调用方法时，会将数据进行封装，把页面数据封装到page对象之中
        eduTeacherService.page(page,null);
        long total = page.getTotal();//得到总记录数
        List<EduTeacher> records = page.getRecords();//得到每页的list集合
        return Result.ok().data("total",total).data("rows",records);
    }


    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public Result pageTeacherCondition(@PathVariable("current") Long current, @PathVariable("limit")Long limit, @RequestBody(required = false) TeacherQuery teacherQuery){
        Page<EduTeacher> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        //多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断值是否为空，不为空就做拼接
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            queryWrapper.le("gmt_create",end);
        }

        queryWrapper.orderByDesc("gmt_create");
        eduTeacherService.page(page, queryWrapper);
        long total = page.getTotal();//得到总记录数
        List<EduTeacher> records = page.getRecords();//得到每页的list集合
        return Result.ok().data("total",total).data("rows",records);
    }

    //添加讲师
    @PostMapping("/addTeacher")
    public Result addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = eduTeacherService.save(eduTeacher);
        return b?Result.ok():Result.error();
    }

    @GetMapping("/getTeacher/{id}")
    public Result getTeacher(@PathVariable("id")String id){
//        try {
//            int i = 10/0;
//        }catch (Exception e){
//            throw new SunsetException(20001,"执行了自定义异常处理");
//        }
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return Result.ok().data("teacher",eduTeacher);
    }

    @PostMapping("/updateTeacher")
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = eduTeacherService.updateById(eduTeacher);
        return b?Result.ok():Result.error();
    }
}

