package com.oracle.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    //检查验证码是否和预期相符
    public static boolean checkVerifyCode(HttpServletRequest request) {
        String verifyCodeExpected = (String) request.getSession()
                .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String verifyCodeActual = HttpServletRequestUtil.getString(request,
                "verifyCodeActual");
        if (verifyCodeActual == null || !verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)) {
            return false;
        }
        return true;
    }

}	
