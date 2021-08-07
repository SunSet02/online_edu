package com.sunset.oss.controller;

import com.sunset.commonutils.Result;
import com.sunset.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
//@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;
    @PostMapping
    public Result uploadOssFile(MultipartFile file){
        String url = ossService.uploadFileAvatar(file);//方法返回阿里云oss里面图片的路径
//        获取上传文件
        return Result.ok().data("url",url);
    }
}
