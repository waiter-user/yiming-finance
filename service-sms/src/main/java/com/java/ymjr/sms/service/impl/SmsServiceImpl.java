package com.java.ymjr.sms.service.impl;

import com.java.ymjr.sms.service.SmsService;
import com.java.ymjr.sms.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public boolean sendShortMessage(String telephone, String code) {
        SmsUtil.sendMes(code,telephone);
        log.info("短信发送成功!");
        return true;
    }
}
