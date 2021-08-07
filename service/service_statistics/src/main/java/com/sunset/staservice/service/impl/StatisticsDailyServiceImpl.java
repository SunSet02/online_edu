package com.sunset.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sunset.commonutils.Result;
import com.sunset.servicebase.exceptionHanlder.SunsetException;
import com.sunset.staservice.client.UcenterClient;
import com.sunset.staservice.entity.StatisticsDaily;
import com.sunset.staservice.mapper.StatisticsDailyMapper;
import com.sunset.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author sunset
 * @since 2021-08-04
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;
    @Override
    public void countRegister(String day) {
//        添加记录前先删除表中的相同日期
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);
        baseMapper.delete(queryWrapper);
//        远程调用得到人数
        Result result = ucenterClient.countRegieter(day);
        Integer countRegister = (Integer) result.getData().get("countRegister");
//        把获取的数据添加到数据库
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO
//创建统计对象
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(countRegister);
        daily.setLoginNum(loginNum);
        daily.setVideoViewNum(videoViewNum);
        daily.setCourseNum(courseNum);
        daily.setDateCalculated(day);
        baseMapper.insert(daily);

    }
//查询数据
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
//        根据条件查询对应数据
        QueryWrapper<StatisticsDaily> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date_calculated",begin,end);
        queryWrapper.select("date_calculated",type);
        List<StatisticsDaily> selectList = baseMapper.selectList(queryWrapper);
//将数据进行封装
//        前端要求数组结构，对应后端的list集合
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();
//        遍历查询出来的所有数据进行封装
        for (int i = 0; i < selectList.size(); i++) {
            StatisticsDaily daily = selectList.get(i);
            date_calculatedList.add(daily.getDateCalculated());
            if (type.equals("login_num")){
                numDataList.add(daily.getLoginNum());
            }else if(type.equals("register_num")){
                numDataList.add(daily.getRegisterNum());
            }else if(type.equals("video_view_num")){
                numDataList.add(daily.getVideoViewNum());
            }else {
                numDataList.add(daily.getCourseNum());
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);
        return map;
    }
}
