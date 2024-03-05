package com.ccyb.teamwork.service;

import com.ccyb.teamwork.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 项目 服务类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
public interface IProjectService extends IService<Project> {
    Boolean addProject(Project project);

    Boolean deleteById(Long pid);

    Boolean deleteBatchByIds(List<Long> pIds);

    Boolean updateProject(Project project);

    Project selectByProjectId(Long pid);
}
