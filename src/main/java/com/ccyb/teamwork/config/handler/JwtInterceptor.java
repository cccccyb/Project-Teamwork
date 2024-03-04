package com.ccyb.teamwork.config.handler;



import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ccyb.teamwork.entity.User;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.exception.ServiceException;
import com.ccyb.teamwork.service.IUserService;
import com.ccyb.teamwork.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;


@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,@Nonnull HttpServletResponse response,@Nonnull Object handler)throws Exception{
        //前端vue将token添加在请求头中
        String token = request.getHeader("token");

        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        // 执行认证
        if (token.isBlank()) {
            throw new ServiceException(ResponseCode.TOKEN_IS_ILLEGAL, "无token，请重新登录");
        }

        String userId=null;
        // 认证通过
        try {
            DecodedJWT decodedJWT = JWTUtils.verifyToken(token);// 验证token
            // 获取 token 中的 user id
            userId = decodedJWT.getClaim("userId").asString();
            Date expiresAt = decodedJWT.getExpiresAt();
            System.out.println("认证通过：");
            System.out.println("userId: "+userId);
            System.out.println("过期时间：" + expiresAt);
        } catch (JWTVerificationException e) {
        //认证失败
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().append(JSON.toJSONString(ResponseResult.build(ResponseCode.TOKEN_IS_ILLEGAL, "token验证失败，请重新登录",null)));
        System.out.println("认证失败，未通过拦截器!");
        }

        // 根据token中的userid查询数据库
        User user = userService.getById(userId);
        if (user == null) {
            throw new ServiceException(ResponseCode.LOGIN_USERNAME_PASSWORD_ERROR, "用户不存在，请重新登录");
        }

        return true;
    }

}
