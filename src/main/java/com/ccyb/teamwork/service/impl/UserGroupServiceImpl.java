package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.UserGroup;
import com.ccyb.teamwork.mapper.UserGroupMapper;
import com.ccyb.teamwork.service.IUserGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 中间表-用户-用户组 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements IUserGroupService {

}
