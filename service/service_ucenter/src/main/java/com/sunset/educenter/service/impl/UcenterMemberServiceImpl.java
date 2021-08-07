package com.sunset.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunset.commonutils.JwtUtils;
import com.sunset.commonutils.MD5;
import com.sunset.educenter.entity.UcenterMember;
import com.sunset.educenter.entity.vo.RegisterVo;
import com.sunset.educenter.mapper.UcenterMemberMapper;
import com.sunset.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-07-29
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
//    登录方法
    @Override
    public String login(UcenterMember ucenterMember) {
//        获取登录的手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();
//        数据库中密码是加密的，所以要把用户输入的密码加密后再和数据库中的对比
//        MD5加密，加密后无法解密
        if(StringUtils.isEmpty(mobile)|| StringUtils.isEmpty(password)){
            throw new SunsetException(20001,"登录失败");
        }
//        判断手机号是否正确
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        UcenterMember member = baseMapper.selectOne(queryWrapper);
        if(member==null){
            throw new SunsetException(20001,"手机号不存在");
        }

        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new SunsetException(20001,"密码错误");
        }

        if (member.getIsDisabled()){
            throw new SunsetException(20001,"用户被禁用");
        }
//        登录成功，生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        return jwtToken;
    }

//    注册
    @Override
    public void register(RegisterVo registerVo) {
//获取数据
        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();//手机号
        String nickname = registerVo.getNickname();//昵称
        String password = registerVo.getPassword();//密码
//        非空判断
        if(StringUtils.isEmpty(code)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(nickname)||StringUtils.isEmpty(password)){
            throw new SunsetException(20001,"请填写完整信息，注册失败");
        }
//        手机验证码是否正确
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)){
            throw new SunsetException(20001,"验证码错误");
        }
//        手机号不重复
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer integer = baseMapper.selectCount(queryWrapper);
        if (integer>0){
            throw new SunsetException(20001,"手机号重复，请换个手机号试试");
        }
//        添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://sunset-education.oss-cn-beijing.aliyuncs.com/2021/07/22/43702632d35947238942a6fee2ff43ccfile.png");
        baseMapper.insert(member);
    }
//根据openid查询
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    @Override
    public Integer countRegister(String day) {
        Integer count = baseMapper.countRegister(day);
        return count;
    }
}
