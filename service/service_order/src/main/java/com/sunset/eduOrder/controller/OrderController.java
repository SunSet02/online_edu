package com.sunset.eduOrder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunset.commonutils.JwtUtils;
import com.sunset.commonutils.Result;
import com.sunset.eduOrder.entity.Order;
import com.sunset.eduOrder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/eduOrder/order")
//@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;
//    生成订单方法
    @PostMapping("/createOrder/{courseId}")
    public Result createOrder(@PathVariable String courseId, HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
//        返回订单号
        String orderNo = orderService.createOrders(courseId,userId);
        return Result.ok().data("orderId",orderNo);
    }


//    根据订单id获取订单信息
    @GetMapping("/getOrderInfo/{id}")
    public Result getOrderInfo(@PathVariable String id){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",id);
        Order order = orderService.getOne(queryWrapper);
        return Result.ok().data("item",order);
    }

//    根据课程id和用户id，查询订单状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",1);
        int i = orderService.count(queryWrapper);
        System.out.println("========="+i);
        return i>0;
    }
}

