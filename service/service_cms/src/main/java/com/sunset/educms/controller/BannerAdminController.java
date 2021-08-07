package com.sunset.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunset.commonutils.Result;
import com.sunset.educms.entity.CrmBanner;
import com.sunset.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 后台banner管理接口
 * </p>
 *
 * @author sunset
 * @since 2021-07-27
 */
@RestController
@RequestMapping("/educms/banneradmin")
//@CrossOrigin
public class BannerAdminController {
    @Autowired
    private CrmBannerService crmBannerService;

//    分页查询
    @GetMapping("/pageBanner/{page}/{limit}")
    public Result pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> pageBanner = new Page<>(page,limit);
        crmBannerService.page(pageBanner,null);
        return Result.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }

//    添加banner
    @PostMapping("/addBanner")
    public Result addBanner(@RequestBody CrmBanner crmBanner){
        crmBannerService.save(crmBanner);
        return Result.ok();
    }

//    根据id查询banner
    @GetMapping("/get/{id}")
    public Result get(@PathVariable String id){
        CrmBanner crmBanner = crmBannerService.getById(id);
        return Result.ok().data("banner",crmBanner);

    }

//    修改bannner
    @PutMapping("/update")
    public Result update(@RequestBody CrmBanner crmBanner){
        crmBannerService.updateById(crmBanner);
        return Result.ok();
    }

//    删除banner
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable String id){
        crmBannerService.removeById(id);
        return Result.ok();
    }

}

