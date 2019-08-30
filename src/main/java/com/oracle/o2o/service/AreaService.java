package com.oracle.o2o.service;

import java.util.List;

import com.oracle.o2o.entity.Area;

public interface AreaService {
    /**
     * 获取区域列表，优先从缓存获取
     *
     * @return
     */
    public List<Area> getAreaList();
}
