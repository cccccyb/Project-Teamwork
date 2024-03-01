package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.Project;
import com.ccyb.teamwork.mapper.ProjectMapper;
import com.ccyb.teamwork.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

}
