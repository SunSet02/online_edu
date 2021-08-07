package com.sunset.eduService.service;

import com.sunset.eduService.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunset.eduService.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoById(String courseId);
//删除章节
    boolean deleteChapter(String chapterId);

    void removeCourseByCourseId(String courseId);
}
