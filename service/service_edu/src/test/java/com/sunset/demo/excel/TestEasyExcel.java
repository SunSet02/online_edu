package com.sunset.demo.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
//    写操作
    @Test
    public void test(){
        //        实现写操作
//        1.设置写入文件夹的位置和 excel名称
        String filename = "E:\\test\\write.xlsx";
//        调用方法,第一个参数文件路径名称，，第二个参数实体类class
        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());
    }

//    读操作
    @Test
    public void test1(){
//        读路径
        String filename = "E:\\test\\write.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }


    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0 ;i< 10; i++){
            DemoData demoData = new DemoData();
            demoData.setSno(i);
            demoData.setName("gjl"+i);
            list.add(demoData);
        }
        return list;
    }
}
