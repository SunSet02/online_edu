package com.sunset.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.eduService.entity.EduTeacher;
import com.sunset.eduService.mapper.EduTeacherMapper;
import com.sunset.eduService.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-07-17
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

//    分页查询讲师
    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        baseMapper.selectPage(pageTeacher,queryWrapper);
//       获取分页数据放到map中
        Map<String,Object> map = new HashMap<>();
        List<EduTeacher> teacherList = pageTeacher.getRecords();//每页数据list集合
        long current = pageTeacher.getCurrent();//当前页
        long size = pageTeacher.getSize();//每页记录数
        long total = pageTeacher.getTotal();//总记录数
        long pages = pageTeacher.getPages();
        boolean hasNext = pageTeacher.hasNext();//是否有下一页
        boolean hasPrevious = pageTeacher.hasPrevious();//是否有上一页
        map.put("teacherList",teacherList);
        map.put("current",current);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);
        map.put("pages",pages);
        return map;
    }
}
