package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.GroupProject;
import com.ccyb.teamwork.mapper.GroupProjectMapper;
import com.ccyb.teamwork.service.IGroupProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 中间表-用户组-项目 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class GroupProjectServiceImpl extends ServiceImpl<GroupProjectMapper, GroupProject> implements IGroupProjectService {

}
