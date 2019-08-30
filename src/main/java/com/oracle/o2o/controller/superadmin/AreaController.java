package com.oracle.o2o.controller.superadmin;

import com.oracle.o2o.entity.Area;
import com.oracle.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/superadmin")
public class AreaController {

    Logger logger =
            LoggerFactory.getLogger(AreaController.class);
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listarea", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listArea() {
        //一般用info信息记录方法的启动和结束
        logger.info("===start===");
        //记录时间，获取当前时间的毫秒数
        long stratTime = System.currentTimeMillis();
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //用于存放service层返回的区域列表
        List<Area> list = new ArrayList<Area>();
        try {
            list = areaService.getAreaList();
            //这里的key可以随意，但是我们在前端里
            //会用到一个框架叫esayUI,里面的控件是
            //rows,表示的是返回集合，total表示返回的总数
            //所以这里的key是相对固定的
            modelMap.put("rows", list);
            modelMap.put("total", list.size());
        } catch (Exception e) {
            e.printStackTrace();
            //错误的信息给返回，success状态值设为false
            modelMap.put("success", false);
            modelMap.put("errM", e.toString());
        }
        //做测试用
        logger.error("test error!");
        //打印程序运行结束的时间;
        long endTime = System.currentTimeMillis();
        logger.debug("costTime:[{}ms]", endTime - stratTime);
        logger.info("===end===");
        return modelMap;
    }
}
