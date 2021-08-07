package com.sunset.eduService.entity.vo;

import lombok.Data;

@Data
public class CoursePublishVo {
    private String id;
    private static final long serialVersionUID = 1L;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}