package com.oracle.o2o.dao;

import com.oracle.o2o.dao.AreaDao;
import com.oracle.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

//这里没有那些xml文件了，所以BaseTest也没有意义
//所以可以利用Springboot的测试
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea() {
        List<Area> areaList = areaDao.queryArea();
        //判断取出来的areaList数目是2个的话，测试成功
        assertEquals(2, areaList.size());
    }
}
