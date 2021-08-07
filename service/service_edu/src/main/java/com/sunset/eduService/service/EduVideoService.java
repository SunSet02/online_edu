package com.sunset.eduService.service;

import com.sunset.eduService.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeByCourseId(String courseId);
}
