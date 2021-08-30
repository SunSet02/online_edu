package com.sunset.vod;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.junit.Test;

import java.util.List;

public class TeatVod {
    @Test
    public void test(){
//        根据视频id获取视频播放地址
        try {
//            1.创建初始化对象
            DefaultAcsClient defaultAcsClient = InitObject.initVodClient("your keyid", "your keysecreat");
//            2.创建获取视频的request和response对象
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            GetPlayInfoResponse response = new GetPlayInfoResponse();
//            3.向request对象里面设置id值
            request.setVideoId("0a1760feed504bf58d96871b034cdc33");
//            4.调用初始化对象里面的方法，传递request，获取视频播放地址
            response = defaultAcsClient.getAcsResponse(request);
//            5.根据response获取数据
            List<GetPlayInfoResponse.PlayInfo> list = response.getPlayInfoList();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getPlayURL());//获取播放地址
            }
            System.out.println(response.getVideoBase().getTitle());//获取视频名称
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
        try {
            //        根据视频id获取视频播放凭证
            //        创建初始化对象
            DefaultAcsClient defaultAcsClient = InitObject.initVodClient("", "");
//            创建获取视频凭证的request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
//            设置视频id值
            request.setVideoId("5adbc05402e048aab30121542f617476");
//            调用初始化对象的方法
            response = defaultAcsClient.getAcsResponse(request);
//            获取凭证
            String playAuth = response.getPlayAuth();
            System.out.println("playAuth="+playAuth);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
//        视频上传功能测试
        String title = "tset by sdk";//上传到阿里云里面的视频名称
        String fileName = "D:\\BaiduNetdiskDownload\\online_edution\\test_video\\6 - What If I Want to Move Faster.mp4";//本地路径和名称
        UploadVideoRequest request = new UploadVideoRequest("", "", title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }

    }
}
