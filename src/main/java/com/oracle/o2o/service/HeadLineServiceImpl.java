package com.oracle.o2o.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oracle.o2o.dao.HeadLineDao;
import com.oracle.o2o.entity.HeadLine;
import com.oracle.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;

    @Transactional
    public List<HeadLine> getHeadLineList(
            HeadLine headLineCondition) {
        return headLineDao.queryHeadLine(headLineCondition);
    }
}
