package com.sunset.eduService.controller;


import com.sunset.commonutils.Result;
import com.sunset.eduService.entity.EduChapter;
import com.sunset.eduService.entity.EduCourse;
import com.sunset.eduService.entity.chapter.ChapterVo;
import com.sunset.eduService.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduService/chapter")
//@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService;
//课程大纲列表，根据课程id查询
    @GetMapping("/getChapterVideo/{courseId}")
    public Result getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list =  eduChapterService.getChapterVideoById(courseId);
        return Result.ok().data("list",list);
    }
//添加章节
    @PostMapping("/addChapter")
    public Result addChapter(@RequestBody EduChapter eduChapter){
         eduChapterService.save(eduChapter);
         return Result.ok();
    }
//    根据id查询章节、
    @GetMapping("/getChapterInfo/{chapterId}")
    public Result getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return Result.ok().data("chapter",eduChapter);
    }

    //修改章节
    @PostMapping("/updateChapter")
    public Result updateChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.updateById(eduChapter);
        return Result.ok();
    }

    //删除章节
    @DeleteMapping("/{chapterId}")
    public Result deleteChapter(@PathVariable String chapterId){
        boolean flag = eduChapterService.deleteChapter(chapterId);
        if(flag){
            return Result.ok();
        }else {
            return Result.error();
        }

    }

}

