package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.*;
import com.ccyb.teamwork.mapper.UserGroupMapper;
import com.ccyb.teamwork.mapper.UserMapper;
import com.ccyb.teamwork.mapper.UserRoleMapper;
import com.ccyb.teamwork.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    UserGroupMapper userGroupMapper;

    @Override
    public User getByNameAndPassword(String name, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPasswd, password);
        wrapper.eq(User::getUsername, name);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public List<User> getAllUser() {
        return userMapper.selectList(null);
    }

    @Override
    public IPage<User> getAllUserPage(IPage<?> page, String username, List<Long> searchRole, List<Long> searchGroup, Integer enable) {
        return userMapper.getAllUserPage(page,username,searchRole,searchGroup,enable);
    }


    @Override
    public User getUserById(long id) {
        return null;
    }

    @Override
    public boolean addUser(User user) {
        if (userMapper.insert(user) == 0) {
            return false;
        }
        Long userId = user.getId();
        if (!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(role.getId());
                userRole.setUserId(userId);
                if (userRoleMapper.insert(userRole) == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (!user.getGroups().isEmpty()) {
            for (Group group : user.getGroups()) {
                UserGroup userGroup = new UserGroup();
                userGroup.setGroupId(group.getId());
                userGroup.setUserId(userId);
                if (userGroupMapper.insert(userGroup) == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyUser(User user) {
        Long userId = user.getId();
        if (userMapper.updateById(user) == 0) {
            return false;
        }
        if (!user.getRoles().isEmpty()) {
            for (Group group : user.getGroups()) {
                LambdaUpdateWrapper<UserGroup> updateGroupWrapper = new LambdaUpdateWrapper<>();
                updateGroupWrapper.eq(UserGroup::getUserId, userId).set(UserGroup::getGroupId, group.getId());
                if (userGroupMapper.update(updateGroupWrapper) == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }
        if (!user.getGroups().isEmpty()) {
            for (Role role : user.getRoles()) {
                LambdaUpdateWrapper<UserRole> updateRoleWrapper = new LambdaUpdateWrapper<>();
                updateRoleWrapper.eq(UserRole::getUserId, userId).set(UserRole::getRoleId, role.getId());
                if (userRoleMapper.update(updateRoleWrapper) == 0) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }
}
