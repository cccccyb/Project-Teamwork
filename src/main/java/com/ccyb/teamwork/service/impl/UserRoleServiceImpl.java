package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.UserRole;
import com.ccyb.teamwork.mapper.UserRoleMapper;
import com.ccyb.teamwork.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 中间表-用户-角色 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
