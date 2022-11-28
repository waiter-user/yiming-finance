package com.java.ymjr.core.test;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class JwtTest {
    //过期时间，毫秒，24小时
    private static long tokenExpiration = 24*60*60*1000;
    //秘钥
    private static String tokenSignKey = "softeem456";
    @Test
    public void testCreateToken() {
        JwtBuilder jwtBuilder = Jwts.builder();
        //头，载荷，签名哈希
        String token= jwtBuilder.
                //头
                        setHeaderParam("typ", "JWT") //令牌类型
                .setHeaderParam("alg", "HS256") //签名算法
                //载荷,默认信息
                .setSubject("ymjr-user") //令牌主题
                .setIssuer("softeem")//签发者
                .setAudience("lxg")//接收者
                .setIssuedAt(new Date())//签发时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) //过期时间
                .setNotBefore(new Date(System.currentTimeMillis() + 20 * 1000)) //20秒后可用
                .setId(UUID.randomUUID().toString())
                //载荷,自定义信息
                .claim("nickName", "Jesonback")
                .claim("avatar", "1.jpg")
                //签名哈希
                .signWith(SignatureAlgorithm.HS256, tokenSignKey)
                .compact(); //转换成字符串
        System.out.println(token);
    }

    @Test
    public void testGetInfo() {
        //要解析的jwt
        String jwt="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5bWpyLXVzZXIiLCJpc3MiOiJzb2Z0ZWVtIiwiYXVkIjoibHhnIiwiaWF0IjoxNjY1MDI1Mjk1LCJleHAiOjE2NjUxMTE2OTUsIm5iZiI6MTY2NTAyNTMxNSwianRpIjoiZDA1ZWU0MjAtN2I3MS00NDU2LWE0ZTEtZGQ5MGMyNmNiOWRiIiwibmlja05hbWUiOiJKZXNvbmJhY2siLCJhdmF0YXIiOiIxLmpwZyJ9.dKmxSFguFT7VNJ-F5B8WvLqp1fYAGJmAIAkjw1duPOU";
        String token = jwt;
        //获取jwt解析器
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(tokenSignKey).parseClaimsJws(token);
        //解析JWT头
        JwsHeader header = claimsJws.getHeader();
        String algorithm = header.getAlgorithm();
        String type = header.getType();
        System.out.println(algorithm+"..."+type);
        System.out.println("********************************");
        //解析有效载荷
        Claims claims = claimsJws.getBody();
        String subject = claims.getSubject();
        String issuer = claims.getIssuer();
        String audience = claims.getAudience();
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        Date notBefore = claims.getNotBefore();
        String id = claims.getId();

        System.out.println(subject);
        System.out.println(issuer);
        System.out.println(audience);
        System.out.println(issuedAt);
        System.out.println(expiration);
        System.out.println(notBefore);
        System.out.println(id);
        String nickname = (String)claims.get("nickName");
        String avatar = (String)claims.get("avatar");

        System.out.println("昵称："+nickname);
        System.out.println("头像："+avatar);
        System.out.println("********************************");

        //解析签名哈希
        String signature = claimsJws.getSignature();
        System.out.println(signature);
    }
}
