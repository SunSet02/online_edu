package com.sunset.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunset.eduService.client.VodClient;
import com.sunset.eduService.entity.EduVideo;
import com.sunset.eduService.mapper.EduVideoMapper;
import com.sunset.eduService.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;
//    根据课程id删除小节，同时删除阿里云里面的视频
    @Override
    public void removeByCourseId(String courseId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
//        根据课程id查询出video表中的所有记录
        wrapper.eq("course_id",courseId);
        wrapper.select("video_source_id");//只查询video_sourse_id
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);

        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                videoIds.add(videoSourceId);
            }
        }
        if (videoIds.size()>0){
            vodClient.deleteBatch(videoIds);
        }


        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }
}
