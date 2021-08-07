package com.sunset.eduOrder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.sunset.eduOrder.entity.Order;
import com.sunset.eduOrder.entity.PayLog;
import com.sunset.eduOrder.mapper.PayLogMapper;
import com.sunset.eduOrder.service.OrderService;
import com.sunset.eduOrder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunset.eduOrder.utils.HttpClient;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-08-02
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;
    @Override
    public Map createPay(String orderNo) {
//        引入微信支付相关依赖
        try {
//            根据订单号，查询订单信息
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(queryWrapper);
//            使用map设置二维码需要的参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());//课程标题
            m.put("out_trade_no", orderNo);//订单号
            m.put("total_fee", order.getTotalFee().multiply(new
                    BigDecimal("100")).longValue()+"");//订单号
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url",
                    "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");
//            使用httpclient请求，发送xml格式参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
//           执行post发送请求
            client.post();
//            根据请求得到返回的结果，从结果里面得到数据
//            返回的内容是xml格式的
            String content = client.getContent();
//            将xml格式转换为map集合
            Map<String,String> resultMap =WXPayUtil.xmlToMap(content);
            Map map = new HashMap();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));//返回二维码状态码
            map.put("code_url", resultMap.get("code_url"));//二维码地址
            return map;
//            #######二维码map集合{
//            course_id=1421675131759042562,
//            out_trade_no=20210803101017478,
//            code_url=weixin://wxpay/bizpayurl?pr=eu6A9Rjzz,
//            total_fee=0.01, result_code=SUCCESS}

        }catch (Exception e){
            throw new SunsetException(20001,"生成二维码失败");
        }
    }
//根据订单号，查询订单支付状态
    @Override
    public Map<String, String> querrPayStatus(String orderNo) {
        try {
//            封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
//            发送httpclient请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
//            返回第三方数据
            String content = client.getContent();
//            将xml装换成map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            return resultMap;

        }catch (Exception e){
            throw new SunsetException(20001,"支付失败了");
        }
    }
//添加支付记录，更新订单支付状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
//      微信固定地址，返回数据的map中有订单号，支付状态，流水号
        String orderNo = map.get("out_trade_no");
//        根据订单号，查询订单信息
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(queryWrapper);
//        更新订单表，订单的状态
        if (order.getStatus().intValue()==1){
            return;
        }
        order.setStatus(1);//1代表已经支付，0表示未支付
        orderService.updateById(order);
//        向支付表添加记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));//订单流水号
        payLog.setAttr(JSONObject.toJSONString(map));//其他数据
        baseMapper.insert(payLog);
    }
}
