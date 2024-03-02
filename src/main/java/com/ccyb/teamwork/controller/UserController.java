package com.ccyb.teamwork.controller;

import com.ccyb.teamwork.entity.User;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IUserService;
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
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public ResponseResult<User> login(@RequestBody User user){
        String name = user.getUsername();
        String password = user.getPasswd();
        User userDB = userService.getByNameAndPassword(name,password);
        int code = userDB != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = userDB != null ? "登录成功" : "查询不到该用户，请重试！";
        return ResponseResult.build(code, msg, userDB);
    }

}
