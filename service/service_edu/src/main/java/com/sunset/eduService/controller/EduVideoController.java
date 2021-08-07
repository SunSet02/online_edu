package com.sunset.eduService.controller;


import com.sunset.commonutils.Result;
import com.sunset.eduService.client.VodClient;
import com.sunset.eduService.entity.EduChapter;
import com.sunset.eduService.entity.EduVideo;
import com.sunset.eduService.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
@RestController
@RequestMapping("/eduService/video")
//@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired//注入服务的interfacee
    private VodClient vodClient;
    @PostMapping("/addVideo")
    public Result addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return Result.ok();
    }


    //    根据id查询章节、
    @GetMapping("/getVideoInfo/{id}")
    public Result getVideoInfo(@PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        return Result.ok().data("video",eduVideo);
    }

    @PostMapping("/updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return Result.ok();
    }
//    删除小节，同时删除阿里云里面的视频
    @DeleteMapping("{id}")
    public Result deleteVideo(@PathVariable String id){
//        根据小节id查询视频id
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            vodClient.removeAlyVideo(videoSourceId);
        }
        eduVideoService.removeById(id);
        return Result.ok();
    }

}

