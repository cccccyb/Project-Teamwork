package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Task;
import com.ccyb.teamwork.mapper.TaskMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccyb.teamwork.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 任务 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    TaskMapper taskMapper;

    @Override
    public Boolean addTask(Task task) {
        task.setItem_type(2);
        return taskMapper.insert(task) > 0;
    }

    @Override
    public Boolean deleteById(Long tId) {
        return taskMapper.deleteById(tId) > 0;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> tIds) {
        return taskMapper.deleteBatchIds(tIds) > 0;
    }

    @Override
    public Boolean updateTask(Task task) {
        Task selectById = taskMapper.selectById(task.getId());
        task.setVersion(selectById.getVersion());
        task.setModifyTime(LocalDateTime.now());
        return taskMapper.updateById(task) > 0;
    }

    @Override
    public Task selectTaskById(Long tId) {
        return taskMapper.selectTaskById(tId);
    }

    @Override
    public IPage<Task> selectPageTask(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long iterationId) {
        return taskMapper.selectPageTask(page,title,status,priority,creatorId,processerId,projectId,iterationId);
    }

    @Override
    public Boolean updateTaskStatusById(Long tId, Integer status) {
        if ((null == tId) || (null == status)) {
            return false;
        }
        LambdaUpdateWrapper<Task> luw = new LambdaUpdateWrapper<>();
        luw.eq(Task::getId, tId).set(Task::getStatus, status).set(Task::getModifyTime, LocalDateTime.now());
        return taskMapper.update(null, luw) > 0;
    }

    @Override
    public Boolean updateTaskPriorityById(Long tId, Integer priority) {
        if ((null == tId) || (null == priority)) {
            return false;
        }
        LambdaUpdateWrapper<Task> luw = new LambdaUpdateWrapper<>();
        luw.eq(Task::getId, tId).set(Task::getPriority, priority).set(Task::getModifyTime, LocalDateTime.now());
        return taskMapper.update(null, luw) > 0;
    }
}
