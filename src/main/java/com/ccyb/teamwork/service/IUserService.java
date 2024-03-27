package com.ccyb.teamwork.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    List<User> getAllUser();

    IPage<User> getAllUserPage(IPage<?> page, String username, List<Long> searchRole, List<Long> searchGroup, Integer enable);

    User getUserById(long id);


    boolean addUser(User user);

    boolean modifyUser(User user);

}
