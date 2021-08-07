package com.sunset.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.sunset.commonutils.Result;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import com.sunset.vod.service.VodService;
import com.sunset.vod.utils.ConstantVodUtils;
import com.sunset.vod.utils.InitObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.GET;
import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;
//    上传视频的方法
    @PostMapping("/uploadAlyVideo")
    public Result uploadVideo(MultipartFile file){
//        返回上传视频的id，id供给后面视频点播技术和删除技术使用
        String videoId = vodService.uploadAly(file);
        return Result.ok().data("videoId",videoId);
    }

//    根据视频id删除视频
    @DeleteMapping("/removeAlyVideo/{id}")
    public Result removeAlyVideo(@PathVariable String id){
        try {
//            初始化对象
            DefaultAcsClient defaultAcsClient = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
//            创建request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
//            向request对象中设置id
            request.setVideoIds(id);
//            调用初始化对象方法
            defaultAcsClient.getAcsResponse(request);
            return Result.ok();

        }catch (Exception e){
            e.printStackTrace();
            throw new SunsetException(20001,"删除视频失败");
        }

    }

//    删除多个视频
    @DeleteMapping("/delete-batch")
    public Result deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeAlyVideos(videoIdList);
        return Result.ok();
    }

//    根据视频id获取视频播放凭证
    @GetMapping("/getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable String id){
        try {
//            创建初始化对象
            DefaultAcsClient defaultAcsClient = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //            创建获取视频凭证的request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
//            设置视频id值
            request.setVideoId(id);
//            调用初始化对象的方法
            GetVideoPlayAuthResponse response = defaultAcsClient.getAcsResponse(request);
//            获取凭证
            String playAuth = response.getPlayAuth();
            System.out.println("==========="+playAuth);
            return Result.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw  new SunsetException(20001,"播放视频失败");
        }
    }
}
