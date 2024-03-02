package com.ccyb.teamwork.service;

import com.ccyb.teamwork.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
public interface IUserService extends IService<User> {
    User getByNameAndPassword(String name,String password);

    User getByName(String name);

    void addUser(User user);

}
