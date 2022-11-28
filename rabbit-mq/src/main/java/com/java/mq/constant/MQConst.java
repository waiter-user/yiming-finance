package com.java.mq.constant;

/**
 * 常量类(定义不同异步任务对应的MQ消息常量)
 */
public class MQConst {
    //发短信
    public static final String EXCHANGE_SMS="exchange.sms";//交换机
    public static final String ROUTING_SMS_KEY="routing.sms";//路由键
    public static final String QUEUE_SMS="queue.sms";//队列
}
