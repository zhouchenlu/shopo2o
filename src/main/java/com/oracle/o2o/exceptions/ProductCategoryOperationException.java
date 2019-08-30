package com.oracle.o2o.exceptions;

//是RuntimeException，需要事务来管理
public class ProductCategoryOperationException
        extends RuntimeException {

    private static final long serialVersionUID
            = 1182563719599527969L;

    public ProductCategoryOperationException(String msg) {
        super(msg);
    }
}
