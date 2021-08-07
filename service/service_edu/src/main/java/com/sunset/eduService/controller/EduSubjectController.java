package com.sunset.eduService.controller;


import com.sunset.commonutils.Result;
import com.sunset.eduService.entity.subject.OneSubject;
import com.sunset.eduService.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-07-22
 */
@RestController
@RequestMapping("/eduService/subject")
//@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;


//    添加课程分类
//    获取上传过来的文件，把文件读取出来、
    @PostMapping("/addSubject")
    public Result addSubject(MultipartFile file){
//        上传过来的excel文件
        subjectService.saveSubject(file,subjectService);
        return Result.ok();
    }

//    课程类别添加（树型）
    @GetMapping("/getAllSubject")
    public Result getAllSubject(){
//        list集合的泛型是一级分类
        List<OneSubject> list = subjectService.getAllOneTwoSubject();
        return Result.ok().data("list",list);
    }
}

