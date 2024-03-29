package com.ccyb.teamwork.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccyb.teamwork.entity.Requirement;
import com.ccyb.teamwork.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    IPage<Project> selectPageProject(IPage<Project> page, Long userId,String name,Integer status, String startTime, String endTime, Long creatorId);

    List<Requirement> selectAllItem(String title, Long projectId, Long iterationId);
    Boolean updateProjectStatusById(Long pid,Integer status);

    IPage<User> selectPageProjectMembers(IPage<?> page, String username, Long projectId);

}
