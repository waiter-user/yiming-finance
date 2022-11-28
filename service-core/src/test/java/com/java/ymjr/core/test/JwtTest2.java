package com.java.ymjr.core.test;

import com.java.ymjr.base.redis.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class JwtTest2 {
    @Test
    public void test1() {
        String token = JwtUtil.createToken(001L, "Mike");
        System.out.println(token);
    }

    @Test
    public void test2() {
        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSivT1CtINDXYNUtJRSq0oULIyNDMzNTQ0NjYz11EqLU4t8kwBikGYfom5qUAtvpnZqUq1AHT1vBRBAAAA.nKMWnS07RARMtK7oEIss1Vs7FH2g4MBiPijdhCnmsB5bppRMnKPG8oxO5F3uXq56rN45SrAmqb7FdjmLUC0qZA";
        Long userId = JwtUtil.getUserId(token);
        String userName = JwtUtil.getUserName(token);
        System.out.println("username:" + userName + "," + "userId:" + userId);
    }
    @Test
    public void test3(){
        Object o=null;
        Assert.notNull(o,"非法的参数");
    }
    @Test
    public void test4(){
        int a=2,b=6;
        Assert.isTrue(a==b,"两个数不相等");
    }
}
