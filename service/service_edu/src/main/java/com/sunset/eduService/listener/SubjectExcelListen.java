package com.sunset.eduService.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunset.eduService.entity.EduSubject;
import com.sunset.eduService.entity.excel.SubjectData;
import com.sunset.eduService.service.EduSubjectService;
import com.sunset.servicebase.exceptionHanlder.SunsetException;

public class SubjectExcelListen extends AnalysisEventListener<SubjectData> {
    private EduSubjectService eduSubjectService;

    public SubjectExcelListen() {
    }

    public SubjectExcelListen(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData == null){
            throw new SunsetException(20001,"文件数据为空");
        }
//        判断一级分类是否重复
        EduSubject eduSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if(eduSubject==null){
            eduSubject = new EduSubject();
            eduSubject.setParentId("0");
            eduSubject.setTitle(subjectData.getOneSubjectName());
            eduSubjectService.save(eduSubject);
        }
//        获取一级分类id值
        String pid = eduSubject.getId();
        EduSubject twoSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), pid);
        if(twoSubject == null){
            twoSubject = new EduSubject();
            twoSubject.setParentId(pid);
            twoSubject.setTitle(subjectData.getTwoSubjectName());
            eduSubjectService.save(twoSubject);
        }

    }

//    一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject one = subjectService.getOne(wrapper);
        return one;
    }

    //    二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","pid");
        EduSubject two = subjectService.getOne(wrapper);
        return two;
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
