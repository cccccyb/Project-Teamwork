package com.ccyb.teamwork.config.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ccyb.teamwork.entity.User;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.exception.ServiceException;
import com.ccyb.teamwork.service.IUserService;
import com.ccyb.teamwork.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        //前端vue将token添加在请求头中
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // token为空
        if (StringUtils.isBlank(token)) {
            log.error("无token，请重新登录!");
            throw new ServiceException(ResponseCode.TOKEN_IS_BLANK, "无token，请重新登录");
        }


        // token非空

        DecodedJWT decodedJWT = JWTUtils.verifyToken(token);// 验证token
        if (null != decodedJWT) {
            // 获取 token 负载中的 userId
            User user = userService.getById(decodedJWT.getClaim("userId").asLong());
            if (user == null) {
                log.error("用户不存在，请重新登录!");
                throw new ServiceException(ResponseCode.LOGIN_USERNAME_PASSWORD_ERROR, "用户不存在，请重新登录");
            }else{
                boolean tokenIsExpired = JWTUtils.isTokenExpired(decodedJWT.getExpiresAt());
                if (!tokenIsExpired){
                    return true;
                }

            }
        }

        return true;
    }

}
