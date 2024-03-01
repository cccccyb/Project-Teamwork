package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.ProjectUser;
import com.ccyb.teamwork.mapper.ProjectUserMapper;
import com.ccyb.teamwork.service.IProjectUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 中间表-用户-项目 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserMapper, ProjectUser> implements IProjectUserService {

}
