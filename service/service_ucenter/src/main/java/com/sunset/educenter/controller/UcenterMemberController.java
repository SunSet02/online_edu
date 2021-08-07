package com.sunset.educenter.controller;


import com.sunset.commonutils.JwtUtils;
import com.sunset.commonutils.Result;
import com.sunset.commonutils.ordervo.UcenterMemberVo;
import com.sunset.commonutils.vo.CommentUcenterVo;
import com.sunset.educenter.entity.UcenterMember;
import com.sunset.educenter.entity.vo.RegisterVo;
import com.sunset.educenter.service.UcenterMemberService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-07-29
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

//    登录
    @PostMapping("/login")
    public Result loginUser(@RequestBody UcenterMember ucenterMember){
//        调用方法实现登录
//        返回token实现单点登录,使用token
        String token = ucenterMemberService.login(ucenterMember);
        return Result.ok().data("token",token);
    }


//    注册
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return Result.ok();
    }

//    根据token获取用户信息
    @GetMapping("/getMemberInfo")
    public Result getMemberInfo(HttpServletRequest request){
//        根据jwt工具类，返回用户id
        String id = JwtUtils.getMemberIdByJwtToken(request);
//        查询数据库根据id获取信息
        UcenterMember member = ucenterMemberService.getById(id);
        return Result.ok().data("userInfo",member);
    }

//    评论功能，根据用户的id返回用户的昵称，头像
    @GetMapping("/getCommentInfo/{id}")
    public CommentUcenterVo getCommentUcenterInfo(@PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        CommentUcenterVo commentUcenterVo = new CommentUcenterVo();
        BeanUtils.copyProperties(ucenterMember,commentUcenterVo);
        return commentUcenterVo;
    }

//    订单功能，根据用户id获取用户信息
    @GetMapping("/getOrderUserInfo/{id}")
    public UcenterMemberVo getOrderUserInfo(@PathVariable String id){
        UcenterMember member = ucenterMemberService.getById(id);
        UcenterMemberVo ucenterMemberVo = new UcenterMemberVo();
        BeanUtils.copyProperties(member,ucenterMemberVo);
        return ucenterMemberVo;
    }

//    查询某一天的注册人数
    @GetMapping("/countRegieter/{day}")
    public Result countRegieter(@PathVariable String day){
        Integer count = ucenterMemberService.countRegister(day);
        return Result.ok().data("countRegister",count);
    }

}

