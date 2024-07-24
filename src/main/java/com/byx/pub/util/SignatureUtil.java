package com.byx.pub.util;

import cn.hutool.core.lang.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jamin
 * @description 自定义签名验证，用于重定向操作，和企业微信的签名验证无关
 * @Date 2023/2/3 14:59
 */
public class SignatureUtil {

    private static String token = ".xelnik.nc!";

    public static Integer success = 0;

    public static Integer paramsError = 1;
    public static String paramsErrorMsg = "参数不正确";

    public static Integer expiredRequest = 2;
    public static String expiredRequestMsg = "该请求已失效";

    public static Integer invalidRequest = 3;
    public static String invalidRequestMsg = "无效的请求";

    /**
     * 生成签名signature：这是为了保证此验证链接的安全性，用户及第三方均无法伪造。
     *
     * @param value
     * @param timestamp
     * @return
     */
    public static String getSignature(String value, String timestamp) {
        String signature = "";

        // 生成签名--开始
        List<String> params = new ArrayList<String>();
        params.add(token);
        params.add(value);
        params.add(timestamp);
        Collections.sort(params, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        String temp = params.get(0) + params.get(1) + params.get(2);
        signature = SHA1.encode(temp);
        // 生成签名--结束

        return signature;
    }

    /**
     * 验证签名
     * @param value
     * @param timestamp
     * @param signature
     * @param vaildms 有效的毫秒数
     */
    public static Integer verifySignature(String value, String timestamp, String signature, Long vaildms) {
        // 验证参数
        if(Validator.isNull(value) || Validator.isNull(timestamp) || Validator.isNull(signature)) {
            return paramsError;
        }
        long currentTime = System.currentTimeMillis();
        long tempTime = new Long(timestamp);
        long diff = currentTime-tempTime;	// 相差的毫秒数
        if(diff>vaildms){
            return expiredRequest;
        }
        // 验证签名
        String newSignature = getSignature(value, timestamp);
        if(!newSignature.equals(signature)) {
            return invalidRequest;
        }
        return success;
    }
}
