package com.sunset.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.commonutils.JwtUtils;
import com.sunset.commonutils.Result;
import com.sunset.commonutils.vo.CommentUcenterVo;
import com.sunset.eduService.client.UcenterClient;
import com.sunset.eduService.entity.EduComment;
import com.sunset.eduService.service.EduCommentService;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/eduService/comment")
//@CrossOrigin
public class EduCommentController {
    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;
//    根据课程id查询课程评论
    @GetMapping("/pageComment/{page}/{limit}")
    private Result pageComment(@PathVariable long page,@PathVariable long limit,String courseId){
        Page<EduComment> eduCommentPage = new Page<>(page,limit);
        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        commentService.page(eduCommentPage,queryWrapper);
        List<EduComment> commentList = eduCommentPage.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", eduCommentPage.getCurrent());
        map.put("pages", eduCommentPage.getPages());
        map.put("size", eduCommentPage.getSize());
        map.put("total", eduCommentPage.getTotal());
        map.put("hasNext", eduCommentPage.hasNext());
        map.put("hasPrevious", eduCommentPage.hasPrevious());
        return Result.ok().data("map",map);
    }

//    添加评论
    @PostMapping("/addComment")
    public Result addComment(@RequestBody EduComment eduComment, HttpServletRequest request){
//        因为登录的时候将生成的用户的token令牌放到了header里面，所以可以使用自己的jwt工具类返回得到用户id
        String id = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(id)){
            throw new SunsetException(20001,"请先登录");
        }
        eduComment.setMemberId(id);
//        使用springcloud远程调用
        CommentUcenterVo userInfo = ucenterClient.getCommentUcenterInfo(id);
        eduComment.setNickname(userInfo.getNickName());
        eduComment.setAvatar(userInfo.getAvatar());
        commentService.save(eduComment);
        return Result.ok();
    }

}

