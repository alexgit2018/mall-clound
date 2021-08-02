package com.common.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

/**
 * jwt token 生成工具类
 */
public class JwtTokenUtil {

    //秘钥
    static final String SECRET = "CHARDANCETOKEN20201211##";
    //签名由谁生成
    static final String ISSUSER = "mall";
    //签名的主题
    static final String SUBJECT = "mall token";
    //签名面向
    static final String AUDIENCE = "MALLCLIENT";
    //有效期为
    static final Integer JWT_TTL = 30;

    /**
     签发对象：这个用户的id
     签发时间：现在
     有效时间：30分钟
     载荷内容：暂时设计为：这个人的名字，这个人的昵称
     加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(String userId, String userName,String password) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE,JWT_TTL);
        Date expiresDate = nowTime.getTime();

        return JWT.create().withAudience(userId)   //签发对象
                .withIssuedAt(new Date())    //发行时间
                .withExpiresAt(expiresDate)  //有效时间
                .withClaim("userName", userName)    //载荷，随便写几个都可以
                .withClaim("userId",userId)
                .withClaim("userPwd",password)
                .sign(Algorithm.HMAC256(userId+SECRET));   //加密
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     * @param token
     * @param secret
     */
    public static Boolean verifyToken(String token, String secret){

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret+SECRET)).build();
            verifier.verify(token);
            return  true;
        } catch (Exception e) {
            e.getMessage();
        }
        return  false;
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (Exception j) {

        }
        return audience;
    }


    public static String getUserName(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getClaim("userName").asString();
        } catch (Exception j) {

        }
        return audience;
    }

    public static String getUserId(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getClaim("userId").asString();
        } catch (Exception j) {

        }
        return audience;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }

    public static String getStringByName(String token, String name){
        return JWT.decode(token).getClaim(name).asString();
    }
}
