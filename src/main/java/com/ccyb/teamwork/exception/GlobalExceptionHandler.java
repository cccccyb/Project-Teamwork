package com.ccyb.teamwork.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局的异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseResult<?> handleServiceException(ServiceException ex) {
        return ResponseResult.build(ex.getCode(), ex.getMessage(), null);
    }

    @ExceptionHandler(DataValidationFailedException.class)
    public ResponseResult<?> handleDataValidationFailedException(DataValidationFailedException ex) {
        return ResponseResult.build(ResponseCode.DATA_VALIDATION_FAILED, ex.getMessage(), null);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseResult<?> handleJWTVerifyException(JWTVerificationException ex) {
        return switch (ex.getClass().getSimpleName()) {
            //token已过期
            case "TokenExpiredException" -> ResponseResult.build(ResponseCode.TOKEN_HAS_EXPIRED, "token已过期", null);
            //jwt格式错误
            case "JWTDecodeException" -> ResponseResult.build(ResponseCode.TOKEN_Format_ERROR, "token格式有误", null);
            //签名异常
            case "SignatureVerificationException" -> ResponseResult.build(ResponseCode.TOKEN_Signature_ERROR, "签名异常", null);
            //解析失败，token无效
            case "InvalidClaimException" -> ResponseResult.build(ResponseCode.TOKEN_IS_ILLEGAL, ex.getMessage(), null);
            default -> ResponseResult.build(ResponseCode.SYSTEM_ERROR, ex.getMessage(), null);
        };
    }


}
