package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ccyb.teamwork.entity.Project;
import com.ccyb.teamwork.entity.ProjectUser;
import com.ccyb.teamwork.mapper.ProjectMapper;
import com.ccyb.teamwork.mapper.ProjectUserMapper;
import com.ccyb.teamwork.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    ProjectUserMapper projectUserMapper;

    @Override
    public Boolean addProject(Project project) {
        boolean addFlag;
        addFlag = projectMapper.insert(project) > 0;
        return addFlag;
    }

    @Override
    public Boolean deleteById(Long pid) {
        LambdaQueryWrapper<ProjectUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ProjectUser::getProjectId, pid);
        projectUserMapper.delete(lqw);
        return projectMapper.deleteById(pid) > 0;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> pIds) {
        for (Long pid :
                pIds) {
            LambdaQueryWrapper<ProjectUser> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ProjectUser::getProjectId, pid);
            projectUserMapper.delete(lqw);
        }
        return projectMapper.deleteBatchIds(pIds) > 0;
    }

    @Override
    @Transactional
    public Boolean updateProject(Project project) {
        Project selectById = projectMapper.selectById(project.getId());
        project.setVersion(selectById.getVersion());
        return projectMapper.updateById(project) > 0;
    }

    @Override
    public Project selectByProjectId(Long pid) {
        return projectMapper.selectProjectById(pid);
    }


}
