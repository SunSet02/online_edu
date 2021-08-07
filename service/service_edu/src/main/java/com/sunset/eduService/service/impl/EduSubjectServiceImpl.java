package com.sunset.eduService.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunset.eduService.entity.EduSubject;
import com.sunset.eduService.entity.excel.SubjectData;
import com.sunset.eduService.entity.subject.OneSubject;
import com.sunset.eduService.entity.subject.TwoSubject;
import com.sunset.eduService.listener.SubjectExcelListen;
import com.sunset.eduService.mapper.EduSubjectMapper;
import com.sunset.eduService.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-07-22
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class,new SubjectExcelListen(subjectService)).sheet().doRead();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
//返回指定数据
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
//        1.查询出所有的一级分类 parent_id = 0
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id",0);
        List<EduSubject> oneList = baseMapper.selectList(wrapper1);
//        2.查询出所有的二级分类 parent_id !=0
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id",0);
        List<EduSubject> twoList = baseMapper.selectList(wrapper2);
//        创建list集合，用于最后封装的数据
        List<OneSubject> finalList = new ArrayList<>();
//        3.封装一级分类
        for (int i = 0; i < oneList.size(); i++) {//遍历集合，得到每个对象
            EduSubject eduSubject = oneList.get(i);
//            String id = eduSubject.getId();
//            String title = eduSubject.getTitle();
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(id);
//            oneSubject.setTitle(title);
//            使用工具类封装
            BeanUtils.copyProperties(eduSubject, oneSubject);
//            在一级分类里面封装二级分类
            List<TwoSubject> twoSubjectList = new ArrayList<>();
            for (int j = 0; j < twoList.size(); j++) {
                EduSubject eduSubject1 = twoList.get(j);
                if (eduSubject1.getParentId().equals(eduSubject.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(eduSubject1, twoSubject);
                    twoSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoSubjectList);
            finalList.add(oneSubject);
        }

//        4.封装二级分类
        return finalList;
    }
}
