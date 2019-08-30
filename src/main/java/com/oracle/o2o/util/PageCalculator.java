package com.oracle.o2o.util;

public class PageCalculator {

    public static int calculateRowIndex(int pageIndex,
                                        int pageSize) {
        //假如pageIndex=0，说明从第0条开始显示
        //一页显示*pageSize5条
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }

}
