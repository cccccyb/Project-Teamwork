package com.ccyb.teamwork.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ccyb.teamwork.entity.User;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IUserService;
import com.ccyb.teamwork.util.JWTUtils;
import com.ccyb.teamwork.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    //验证token
    @PostMapping("/authentication")
    public ResponseResult<?> authentication(){
        return ResponseResult.build(ResponseCode.SYSTEM_OK,"token验证成功",null);
    }

    //登录验证、生成token
    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody User user){
        String name = user.getUsername();
        String password = user.getPasswd();
        User userLogin = userService.getByNameAndPassword(name,password);
        int code = userLogin != null ? ResponseCode.LOGIN_SUCCESS : ResponseCode.LOGIN_USERNAME_PASSWORD_ERROR;
        // 用户登陆后将id返回给前端
        String msg = userLogin != null ? String.valueOf(userLogin.getId()) : "用户不存在，请重新登录！";
        String token=null;
        if (null!=userLogin){
            token = JWTUtils.sign(userLogin);
        }
        return ResponseResult.build(code, msg, token);
    }

    //获取所有用户
    @GetMapping("/getAll")
    public ResponseResult<?> getAllUser(){
        List<User> allUser = userService.getAllUser();
        int code = allUser != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = allUser != null ? "" : "查询失败";
        return ResponseResult.build(code, msg, allUser);
    }

    //分页查询所有用户，及其用户组、角色及模糊查询
    @GetMapping("/page")
    public ResponseResult<List<User>> getAllUserPage(Integer currentPage, Integer pageSize, String username, String searchRole, String searchGroup, Integer enable) {
        List<Long> roleIds = WebUtil.convertStringToList(searchRole, Long.class);
        List<Long> groupIds = WebUtil.convertStringToList(searchGroup, Long.class);

        Page<User> userPage;
        if (null != currentPage && null != pageSize) {
            userPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            userPage = PageDTO.of(1, -1);
        }
        IPage<User> allUserPage = userService.getAllUserPage(userPage, username.trim(), roleIds, groupIds, enable);
        int code = allUserPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = allUserPage.getRecords() != null ? String.valueOf(allUserPage.getRecords().size()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, allUserPage.getRecords());
    }

}
