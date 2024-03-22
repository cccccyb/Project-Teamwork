package com.ccyb.teamwork.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ccyb.teamwork.entity.Task;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.ITaskService;
import com.ccyb.teamwork.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 任务 前端控制器
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    ITaskService taskService;

    //添加任务
    @PostMapping("/addTask")
    public ResponseResult<?> addTask(@RequestBody Task task) {
        Boolean addTask = taskService.addTask(task);
        String msg = addTask ? "" : "数据添加失败，请重试！";
        return ResponseResult.build(addTask ? ResponseCode.DATABASE_SAVE_OK : ResponseCode.DATABASE_SAVE_ERROR, msg, null);
    }

    //删除任务
    @DeleteMapping("/{tId}")
    public ResponseResult<?> deleteByTaskId(@PathVariable Long tId) {
        Boolean removeById = taskService.deleteById(tId);
        String msg = removeById ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(removeById ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //批量删除任务
    @PostMapping("/batch")
    public ResponseResult<?> deleteBatchByIds(@RequestBody List<String> taskIds) {
        List<Long> tIds = WebUtil.convertStringToLong(taskIds);
        Boolean deleteBatchByIds = taskService.deleteBatchByIds(tIds);
        String msg = deleteBatchByIds ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(deleteBatchByIds ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //修改任务
    @PutMapping("/updateTask")
    public ResponseResult<?> updateTask(@RequestBody Task task) {
        Boolean updateById = taskService.updateTask(task);
        String msg = updateById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据任务id修改任务状态
    @GetMapping("/updateStatus")
    public ResponseResult<?> updateTaskStatusById(String tId,Integer status) {
        Long taskId = null;
        if (StringUtils.hasText(tId)) {
            taskId = Long.parseLong(tId);
        }
        Boolean updateStatusById = taskService.updateTaskStatusById(taskId,status);
        String msg = updateStatusById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateStatusById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据任务id修改任务优先级
    @GetMapping("/updatePriority")
    public ResponseResult<?> updateTaskPriorityById(String tId,Integer priority) {
        Long taskId = null;
        if (StringUtils.hasText(tId)) {
            taskId = Long.parseLong(tId);
        }
        Boolean updatePriorityById = taskService.updateTaskPriorityById(taskId,priority);
        String msg = updatePriorityById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updatePriorityById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据任务id查任务信息及负责人
    @GetMapping("/{tId}")
    public ResponseResult<Task> selectByTaskId(@PathVariable Long tId) {
        Task task = taskService.selectTaskById(tId);
        int code = task != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = task != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, task);
    }

    //分页查询所有任务或分页模糊查询
    @GetMapping("/page")
    public ResponseResult<List<Task>> selectPageTask(Integer currentPage, Integer pageSize,  String title, Integer status, Integer priority, String creatorId, String processerId, String projectId, String iterationId) {
        Long creId = StringUtils.hasText(creatorId)?Long.parseLong(creatorId):null;
        Long proId = StringUtils.hasText(processerId)?Long.parseLong(processerId):null;
        Long pId = StringUtils.hasText(projectId)?Long.parseLong(projectId):null;
        Long iteId = StringUtils.hasText(iterationId)?Long.parseLong(iterationId):null;

        Page<Task> tasktPage;
        if (null != currentPage && null != pageSize) {
            tasktPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            tasktPage = PageDTO.of(1, -1);
        }
        IPage<Task> taskIPage = taskService.selectPageTask(tasktPage, title.trim(), status,priority,creId,proId,pId,iteId);
        int code = taskIPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = taskIPage.getRecords() != null ? String.valueOf(taskIPage.getTotal()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, taskIPage.getRecords());
    }
}
