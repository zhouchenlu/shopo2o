package com.oracle.o2o.util;

import javax.servlet.http.HttpServletRequest;

//负责处理request参数
public class HttpServletRequestUtil {
    //取出参数中key的值，转成int
    public static int getInt(HttpServletRequest request,
                             String key) {
        try {
            //转换成整形
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    //取出参数中key的值，转成Long
    public static long getLong(HttpServletRequest request,
                               String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    //取出参数中key的值，转成Double
    public static Double getDouble(HttpServletRequest request,
                                   String key) {
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }
    }

    //取出参数中key的值，转成Boolean
    public static boolean getBoolean(HttpServletRequest request,
                                     String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    //取出参数中key的值，转成String
    public static String getString(HttpServletRequest request,
                                   String key) {
        try {
            String result = request.getParameter(key);
            if (result != null) {
                result = result.trim();
            }
            if ("".equals(result)) {
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
