package com.sunset.eduOrder.service.impl;

import com.sunset.commonutils.ordervo.CourseWebVo;
import com.sunset.commonutils.ordervo.UcenterMemberVo;
import com.sunset.eduOrder.client.EduClient;
import com.sunset.eduOrder.client.UcenterClient;
import com.sunset.eduOrder.entity.Order;
import com.sunset.eduOrder.mapper.OrderMapper;
import com.sunset.eduOrder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunset.eduOrder.utils.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-08-02
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private EduClient eduClient;
    @Autowired
    private UcenterClient ucenterClient;

//    生成订单
    @Override
    public String createOrders(String courseId, String userId) {

//        通过远程调用，获取用户信息
        UcenterMemberVo orderUserInfo = ucenterClient.getOrderUserInfo(userId);
//        通过远程调用，获取课程信息
        CourseWebVo courseInfo = eduClient.getCourseInfo(courseId);
//        创建order对象，向order对象里面set值
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());//订单号
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName(courseInfo.getTeacherName());
        order.setTotalFee(courseInfo.getPrice());
        order.setMemberId(userId);
        order.setMobile(orderUserInfo.getMobile());
        order.setNickname(orderUserInfo.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
