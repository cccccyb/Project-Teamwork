package com.ccyb.teamwork.controller;

import com.ccyb.teamwork.entity.User;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IUserService;
import com.ccyb.teamwork.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;


    @PostMapping("/authentication")
    public ResponseResult<?> authentication(){
        return ResponseResult.build(ResponseCode.SYSTEM_OK,"token验证成功",null);
    }

    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody User user){
        String name = user.getUsername();
        String password = user.getPasswd();
        User userLogin = userService.getByNameAndPassword(name,password);
        int code = userLogin != null ? ResponseCode.LOGIN_SUCCESS : ResponseCode.LOGIN_USERNAME_PASSWORD_ERROR;
        String msg = userLogin != null ? "登录成功" : "用户不存在，请重新登录！";
        String token=null;
        if (null!=userLogin){
            token = JWTUtils.sign(userLogin);
        }
        System.out.println(ResponseResult.build(code, msg, token));
        return ResponseResult.build(code, msg, token);
    }


}
