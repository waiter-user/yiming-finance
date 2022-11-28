package com.java.ymjr.common.pojo;

import org.springframework.util.StringUtils;

public class Assert {
    /**
     * 断言对象不为空
     * 如果对象obj为空，则抛出异常
     *
     * @param obj 待判断对象
     */
    public static void notNull(Object obj, ResponseEnum responseEnum) {
        if (obj == null) {
            throw new YmjrException(responseEnum);
        }
    }

    /**
     * 断言参数不为空
     * 如果参数为空，则抛出异常
     *
     * @param s
     * @param responseEnum
     */
    public static void notEmpty(String s, ResponseEnum responseEnum) {
        if (StringUtils.isEmpty(s)) {
            throw new YmjrException(responseEnum);
        }
    }

    /**
     * 断言表达式为真
     * 如果不为真，则抛出异常
     *
     * @param expression 是否成功
     */
    public static void isTrue(boolean expression, ResponseEnum responseEnum) {
        if (!expression) {
            throw new YmjrException(responseEnum);
        }
    }

    /**
     * 断言两个字符串相等
     * 如果不相等，则抛出异常
     *
     * @param s1
     * @param s2
     * @param responseEnum
     */
    public static void equals(String s1, String s2, ResponseEnum responseEnum) {
        if (!s1.equals(s2)) {
            throw new YmjrException(responseEnum);
        }
    }
}
