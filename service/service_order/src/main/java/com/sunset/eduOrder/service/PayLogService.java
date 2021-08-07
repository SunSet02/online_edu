package com.sunset.eduOrder.service;

import com.sunset.eduOrder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author sunset
 * @since 2021-08-02
 */
public interface PayLogService extends IService<PayLog> {
    //    生成微信二维码接口
    Map createPay(String orderNo);

    Map<String, String> querrPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);
}
