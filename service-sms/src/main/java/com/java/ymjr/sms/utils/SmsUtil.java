package com.java.ymjr.sms.utils;

import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信的工具类
 */
public class SmsUtil {
    private static final String ACCESS_KEY_ID = "RkFo7wGjVkRBM36XA5DFpzHFkLJ5g8mVPqHqgUTHgoVCGCaY1";
    private static final String TIME = "5";
    private static final String SINGNATURE = "软帝信息";
    private static final String TEMPLATEID = "pub_verif_ttl3";

    public static void sendMes(String code,String Phone) {
// 初始化
        Uni.init(ACCESS_KEY_ID); // 若使用简易验签模式仅传入第一个参数即可
        // 设置自定义参数 (变量短信)
        Map<String, String> templateData = new HashMap<String, String>();
        templateData.put("code", code);
        templateData.put("ttl", TIME);

        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(Phone)
                .setSignature(SINGNATURE)
                .setTemplateId(TEMPLATEID)
                .setTemplateData(templateData);

        // 发送短信
        try {
            UniResponse res = message.send();
            System.out.println("发送短信成功，响应码：" + res.code);
        } catch (UniException e) {
            System.out.println("Error: " + e);
            System.out.println("RequestId: " + e.requestId);
        }
    }
}
