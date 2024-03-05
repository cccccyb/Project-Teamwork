package com.ccyb.teamwork.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ccyb.teamwork.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


@Slf4j
public class JWTUtils {


    // 有效期
    private static final long EXPIRE_TIME = 10 * 60 * 60 * 1000; // 10小时
    // 秘钥明文
    private static final String TOKEN_SECRET = "teamwork";
    //签发者
    public static final String ISSUER = "cyb";

    /**
     * 签名生成
     *
     */
    public static String sign(User user) {
            String token = null;
            try {
                Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
                token = JWT.create()
                        .withIssuer(ISSUER)
                        .withClaim("userId", user.getId())
                        .withExpiresAt(expiresAt)
                        // 使用了HMAC256加密算法。
                        .sign(Algorithm.HMAC256(TOKEN_SECRET));
            }catch (Exception e){
                log.error("exception message", e);
            }


        return token;
    }

    /**
     * 签名验证
     *
     */
    public static DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer(ISSUER).build();
            return verifier.verify(token);
    }

}


