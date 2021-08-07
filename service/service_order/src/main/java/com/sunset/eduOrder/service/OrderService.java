package com.sunset.eduOrder.service;

import com.sunset.eduOrder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author sunset
 * @since 2021-08-02
 */
public interface OrderService extends IService<Order> {

    String createOrders(String courseId, String userId);
}
