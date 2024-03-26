package com.ccyb.teamwork.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccyb.teamwork.entity.Task;

import java.util.List;

/**
 * <p>
 * 任务 服务类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
public interface ITaskService extends IService<Task> {
    Boolean addTask(Task task);

    Boolean deleteById(Long tId);

    Boolean deleteBatchByIds(List<Long> tIds);

    Boolean updateTask(Task task);
    Task selectTaskById(Long tId);

    IPage<Task> selectPageTask(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long iterationId);

    Boolean updateTaskStatusById(Long tId,Integer status);

    Boolean updateTaskPriorityById(Long tId,Integer priority);

    List<Task> selectTaskLimit();

}
