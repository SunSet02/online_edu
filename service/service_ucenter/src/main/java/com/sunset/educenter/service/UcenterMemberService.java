package com.sunset.educenter.service;

import com.sunset.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunset.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author sunset
 * @since 2021-07-29
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countRegister(String day);
}
