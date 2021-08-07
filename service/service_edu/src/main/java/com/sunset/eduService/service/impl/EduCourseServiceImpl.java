package com.sunset.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.eduService.entity.EduCourse;
import com.sunset.eduService.entity.EduCourseDescription;
import com.sunset.eduService.entity.EduTeacher;
import com.sunset.eduService.entity.frontvo.CourseVo;
import com.sunset.eduService.entity.frontvo.CourseWebVo;
import com.sunset.eduService.entity.vo.CourseInfoVo;
import com.sunset.eduService.entity.vo.CoursePublishVo;
import com.sunset.eduService.mapper.EduCourseMapper;
import com.sunset.eduService.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-07-23
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private EduVideoService eduVideoService;
// 添加课程信息方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
//        1向课程表添加信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert<=0){
            throw new SunsetException(20001,"添加课程失败");
        }
        String cid = eduCourse.getId();
//        2向课程描述表添加信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(cid);
        eduCourseDescriptionService.save(eduCourseDescription);
        return cid;
    }

//    将指定id的课程信息封装到courseInfoVo中
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        EduCourse eduCourse = baseMapper.selectById(courseId);
        EduCourseDescription byId = eduCourseDescriptionService.getById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        courseInfoVo.setDescription(byId.getDescription());
        return courseInfoVo;
    }
//修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if(i==0){
            throw new SunsetException(20001,"修改信息失败");
        }
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CoursePublishVo getPublishCourseInfo(String courseId) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);
        return publishCourseInfo;
    }

    @Override
    public void removeCourse(String courseId) {
//        根据课程id删小节
        eduVideoService.removeByCourseId(courseId);
//        删章节
        eduChapterService.removeCourseByCourseId(courseId);
//        删描述
        eduCourseDescriptionService.removeById(courseId);
//        删本身
        int i = baseMapper.deleteById(courseId);
        if(i==0){
            throw new SunsetException(20001,"删除失败");
        }
    }
//条件查询带分页，前台
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseVo courseVo) {
        QueryWrapper<EduCourse> queryWrapper= new QueryWrapper<>();
//        条件不空则拼接
        if(!StringUtils.isEmpty(courseVo.getSubjectParentId())){
            queryWrapper.eq("subject_parent_id",courseVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseVo.getSubjectId())){
            queryWrapper.eq("subject_id",courseVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseVo.getBuyCountSort())){
            queryWrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(courseVo.getPriceSort())){
            queryWrapper.orderByDesc("price");
        }
        if(!StringUtils.isEmpty(courseVo.getGmtCreateSort())){
            queryWrapper.orderByDesc("gmt_create");
        }
        baseMapper.selectPage(pageCourse,queryWrapper);
        //       获取分页数据放到map中
        Map<String,Object> map = new HashMap<>();
        List<EduCourse> eduCourses = pageCourse.getRecords();//每页数据list集合
        long current = pageCourse.getCurrent();//当前页
        long size = pageCourse.getSize();//每页记录数
        long total = pageCourse.getTotal();//总记录数
        long pages = pageCourse.getPages();
        boolean hasNext = pageCourse.hasNext();//是否有下一页
        boolean hasPrevious = pageCourse.hasPrevious();//是否有上一页
        map.put("eduCourseList",eduCourses);
        map.put("current",current);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);
        map.put("pages",pages);
        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
