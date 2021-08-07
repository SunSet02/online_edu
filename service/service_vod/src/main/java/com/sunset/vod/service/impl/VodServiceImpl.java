package com.sunset.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.sunset.commonutils.Result;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import com.sunset.vod.service.VodService;
import com.sunset.vod.utils.ConstantVodUtils;
import com.sunset.vod.utils.InitObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    @Override
    public String uploadAly(MultipartFile file) {
        try {
            //        filename：上传文件的原始名称
            String fileName = file.getOriginalFilename();
//        title：阿里云中显示的名称
            String title = fileName.substring(0, fileName.lastIndexOf("."));
//        inputStream：文件输入流
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
            // request.setEcsRegionId("cn-shanghai");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = "";
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
            }
            return videoId;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void removeAlyVideos(List videoIdList) {
        try {
//            初始化对象
            DefaultAcsClient defaultAcsClient = InitObject.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
//            创建request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            String join = StringUtils.join(videoIdList.toArray(), ",");
//            向request对象中设置id
            request.setVideoIds(join);
//            调用初始化对象方法
            defaultAcsClient.getAcsResponse(request);

        }catch (Exception e){
            e.printStackTrace();
            throw new SunsetException(20001,"删除视频失败");
        }
    }
}
