package com.sunset.educms.controller;

import com.sunset.commonutils.Result;
import com.sunset.educms.entity.CrmBanner;
import com.sunset.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前台banner显示
 * </p>
 *
 * @author sunset
 * @since 2021-07-27
 */
@RestController
@RequestMapping("/educms/bannerfront")
//@CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService crmBannerService;

//    查询所有banner
    @GetMapping("/getAll")
    public Result getAll(){
        List<CrmBanner> list = crmBannerService.selectAllBanner();
        return Result.ok().data("list",list);
    }
}
