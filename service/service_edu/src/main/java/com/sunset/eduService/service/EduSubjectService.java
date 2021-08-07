package com.sunset.eduService.service;

import com.sunset.eduService.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunset.eduService.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author sunset
 * @since 2021-07-22
 */
public interface EduSubjectService extends IService<EduSubject> {
//添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();
}
