package com.sunset.educenter.controller;

import com.google.gson.Gson;
import com.sunset.commonutils.JwtUtils;
import com.sunset.educenter.entity.UcenterMember;
import com.sunset.educenter.service.UcenterMemberService;
import com.sunset.educenter.utils.ConstantUtils;
import com.sunset.educenter.utils.HttpClientUtils;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx")
//@CrossOrigin
public class WxApiController {
    @Autowired
    private UcenterMemberService ucenterMemberService;
//    生成微信扫描的二维码
    @GetMapping("/login")
    public String getWxCode(){
//        直接请求微信地址就行,%s相当于占位符
        String url = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
//        对redirecturl编码
        String redirect_url = ConstantUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirect_url = URLEncoder.encode(redirect_url, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String baseUrl = String.format(url, ConstantUtils.WX_OPEN_APP_ID, redirect_url, "sunset");
        return "redirect:"+baseUrl;
    }


//    获取扫描人信息
    @GetMapping("/callback")
    public String callback(String code,String state){
        try{
            //        1.获取code值，code值类似于验证码
//        2.拿着code请求固定地址得到两个值，access_token,openid
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                            "?appid=%s" +
                            "&secret=%s" +
                            "&code=%s" +
                            "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantUtils.WX_OPEN_APP_ID,
                    ConstantUtils.WX_OPEN_APP_SECRET,
                    code);
//        请求拼接好的地址，得到上面的两个值
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
//            把字符串转换成map集合，解析json字符串
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");//根据key取value
            String openid = (String) mapAccessToken.get("openid");
//            判断数据库是否有该用户信息
            UcenterMember member = ucenterMemberService.getOpenIdMember(openid);
            if (member==null){
                //            拿着access_token，和openid在请求地址
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
//            发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");//昵称
                String headimgurl = (String) userInfoMap.get("headimgurl");//头像
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                member.setNickname(nickname);
                ucenterMemberService.save(member);//表中没有数据，则添加
            }
//            使用jwt生成token字符串，通过路径传递字符串
//            System.out.println(member.getId()+"====================");
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            return "redirect:http://localhost:3000?token="+jwtToken;
//            System.out.println(userInfo);
            //{
            //  "openid":"o3_SC5-WrNKyyTUL1_jPLs3WOOU8",
            //  "nickname":"Est,夕阳",
            //  "sex":1,"language":"zh_CN",
            //  "city":"Ganzhou","province":"Jiangxi",
            //  "country":"CN",
            //  "headimgurl":"https://thirdwx.qlogo.cn/mmopen/vi_32/GZbL0pXoJHuZ77wn6slHdKbMf3OF32bHEJE2fkN4c8NwjypFc4KTD2noLutfuY6icPsPibKJlOvtBkZaUI8X7Dicw/132",
            //  "privilege":[],
            //  "unionid":"oWgGz1JMaox-jqvwHmGAsSanlpgU"}

//            System.out.println(accessTokenInfo+"====================");
//            {
    //            "access_token":"47_sYLL82BDuOhh5552b4qgK7_Xc-7RF-a3v9B1UB47OBtYMe21lU84RotF6h8eQVakxaAqa6-8UyzyVMvk2vWPN2EQPxCdPdWu3F4LkHyFkUo",
    //            "expires_in":7200,
    //            "refresh_token":"47_dexelJirxp1zW6w1VB7Qp2xQg_OhebCSA5FIEncLp-rJhaplhyxbvIX0Ja2fFBcJl8KAMtQE7_8cllSwFDEr70qW87sXaX-InhfUPyKIaFk",
    //            "openid":"o3_SC5-WrNKyyTUL1_jPLs3WOOU8",
            //     "scope":"snsapi_login",
            //     "unionid":"oWgGz1JMaox-jqvwHmGAsSanlpgU"
    //         }
//            System.out.println(code);
//            System.out.println(state);
        }catch (Exception e){
            throw new SunsetException(20001,"登录失败啦~~~");
        }

    }
}
