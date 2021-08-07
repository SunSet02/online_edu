package com.sunset.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunset.eduService.entity.EduChapter;
import com.sunset.eduService.entity.EduVideo;
import com.sunset.eduService.entity.chapter.ChapterVo;
import com.sunset.eduService.entity.chapter.VideoVo;
import com.sunset.eduService.mapper.EduChapterMapper;
import com.sunset.eduService.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunset.eduService.service.EduVideoService;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService eduVideoService;

//    课程章节与小节展示，与subject中的类似
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);
        List<ChapterVo> finalList = new ArrayList<>();
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            List<VideoVo> videoVoList = new ArrayList<>();
            for (int j = 0; j < eduVideoList.size(); j++) {
                EduVideo eduVideo = eduVideoList.get(j);
                if(eduChapter.getId().equals(eduVideo.getChapterId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
            finalList.add(chapterVo);
        }

        return finalList;
    }
//删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
//        查询小节表，如果有数据，不让删除
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(queryWrapper);//有数据，count>0
        if (count>0){
            throw new SunsetException(20001,"章节中存在小节，请先删除小节");
        }else {
            baseMapper.deleteById(chapterId);
            return true;
        }
    }

    @Override
    public void removeCourseByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }
}
