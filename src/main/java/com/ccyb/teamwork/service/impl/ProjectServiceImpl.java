package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Project;
import com.ccyb.teamwork.entity.ProjectUser;
import com.ccyb.teamwork.mapper.ProjectMapper;
import com.ccyb.teamwork.mapper.ProjectUserMapper;
import com.ccyb.teamwork.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

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
        Long projectId = project.getId();
        if (!project.getMembers().isEmpty()) {
            for (Long membersId :
                    project.getMembers()) {
                ProjectUser projectUser = new ProjectUser();
                projectUser.setProjectId(projectId);
                projectUser.setUserId(membersId);
                projectUserMapper.insert(projectUser);
            }
        } else {
            addFlag = false;
        }
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

    @Override
    public IPage<Project> selectPageProject(IPage<Project> page, Long userId, String name, Integer status, String startTime, String endTime, Long creatorId) {
        LocalDateTime start = null, end = null;
        if (!Objects.equals(startTime, "") && !Objects.equals(endTime, "")) {
            start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (null==userId){
            //分页查询所有项目、我管理的项目或分页模糊查询
            return projectMapper.selectPageProject(page, name, status, start, end, creatorId);
        }else {
            //根据userId分页查询所参与的项目
            return projectUserMapper.selectPageMyAttend(page, userId, name, status, start, end, creatorId);
        }
    }

    @Override
    public Boolean updateProjectStatusById(Long pid, Integer status) {
        if ((null == pid) || (null == status)) {
            return false;
        }
        LambdaUpdateWrapper<Project> luw = new LambdaUpdateWrapper<>();
        luw.eq(Project::getId, pid).set(Project::getStatus, status);
        return projectMapper.update(null, luw) > 0;
    }


}
