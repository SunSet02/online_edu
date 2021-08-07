package com.sunset.eduOrder.controller;


import com.sunset.commonutils.Result;
import com.sunset.eduOrder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author sunset
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/eduOrder/paylog")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;
//    生成微信二维码接口
    @GetMapping("/createPay/{orderNo}")
    public Result createPay(@PathVariable String orderNo){
//        返回信息，包括二维码和其他信息
        Map map = payLogService.createPay(orderNo);
        System.out.println("#######二维码map集合"+map);
//        #######二维码map集合{course_id=1421675131759042562,
//        out_trade_no=20210803101017478,
//        code_url=weixin://wxpay/bizpayurl?pr=eu6A9Rjzz,
//        total_fee=0.01,
//        result_code=SUCCESS}
        return Result.ok().data("map",map);
    }

//    查询订单支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo){
//        调用微信固定地址
        Map<String,String> map = payLogService.querrPayStatus(orderNo);
        if (map == null){
            return Result.error().message("支付出错了");
        }
//        如果map不为空，通过map获取订单状态
        if (map.get("trade_state").equals("SUCCESS")){//支付成功
            System.out.println("==========订单状态map集合"+map);
//            向支付表加记录，更新订单表状态
            payLogService.updateOrderStatus(map);
//            ==========订单状态map集合{
//            transaction_id=4200001174202108031205223291,
//            nonce_str=YNLId9OGhCDxkaJn,
//            trade_state=SUCCESS, bank_type=OTHERS, openid=oHwsHuAn8RG9QejXYfFloaT23bkk, sign=29CD451A42A14CCDF443D0A295F7E1EA, return_msg=OK, fee_type=CNY, mch_id=1558950191, cash_fee=1, out_trade_no=20210803104850301, cash_fee_type=CNY, appid=wx74862e0dfcf69954, total_fee=1, trade_state_desc=支付成功, trade_type=NATIVE, result_code=SUCCESS, attach=, time_end=20210803104904, is_subscribe=N, return_code=SUCCESS}
            return Result.ok().message("支付成功");
        }
        return Result.ok().code(25000).message("支付中,请稍后");
    }
}

