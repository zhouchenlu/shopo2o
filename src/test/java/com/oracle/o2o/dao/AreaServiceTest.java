package com.oracle.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oracle.o2o.entity.Area;
import com.oracle.o2o.service.AreaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {
    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList() {
        List<Area> areaList = areaService.getAreaList();
        //结合数据库判断返回的第一个元素是否是西苑
        assertEquals("西苑", areaList.get(0).getAreaName());
    }
}
