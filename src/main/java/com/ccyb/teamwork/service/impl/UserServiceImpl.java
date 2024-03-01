package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.User;
import com.ccyb.teamwork.mapper.UserMapper;
import com.ccyb.teamwork.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
