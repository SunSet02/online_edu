package com.sunset.msmservice.controller;

import com.sunset.commonutils.Result;
import com.sunset.msmservice.Utils.RandomUtils;
import com.sunset.msmservice.Utils.SmsMessageUtils;
import com.sunset.msmservice.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
//    发送短信方法
     @GetMapping("/send/{phone}")
    public Result sendMsm(@PathVariable String phone){
//         从redis里面取验证码，取得到做返回，否则发送
         String code = redisTemplate.opsForValue().get(phone);
         if(!StringUtils.isEmpty(code)){
             return Result.ok();
         }
//         生成随机的6位值
         code = RandomUtils.getSixBitRandom();
         boolean isSend = msmService.sendCode(code,phone);
//         SmsMessageUtils.send(phone,code);
         if(isSend==true){
//             发送成功的验证码放到redis设置时间
//             设置有效时间
             redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
             return Result.ok();
         }else {
             return Result.error();
         }

     }
}
