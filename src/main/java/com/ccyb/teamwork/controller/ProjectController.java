package com.ccyb.teamwork.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ccyb.teamwork.entity.Project;
import com.ccyb.teamwork.entity.Requirement;
import com.ccyb.teamwork.entity.User;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.*;
import com.ccyb.teamwork.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 项目 前端控制器
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    IProjectService projectService;
    @Autowired
    IProjectUserService projectUserService;
    @Autowired
    IRequirementService requirementService;
    @Autowired
    ITaskService taskService;
    @Autowired
    IBugService bugService;

    //添加项目
    @PostMapping("/addProject")
    public ResponseResult<?> addProject(@RequestBody Project project) {
        Boolean insertProject = projectService.addProject(project);
        String msg = insertProject ? "" : "数据添加失败，请重试！";
        return ResponseResult.build(insertProject ? ResponseCode.DATABASE_SAVE_OK : ResponseCode.DATABASE_SAVE_ERROR, msg, null);
    }

    //删除项目
    @DeleteMapping("/{pid}")
    public ResponseResult<?> deleteByProjectId(@PathVariable Long pid) {
        Boolean removeById = projectService.deleteById(pid);
        String msg = removeById ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(removeById ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //批量删除项目
    @PostMapping("/batch")
    public ResponseResult<?> deleteBatchByIds(@RequestBody List<String> projectIds) {
        //	List<String>转List<Long>
        List<Long> pIds = WebUtil.convertStringToLong(projectIds);
        Boolean deleteBatchByIds = projectService.deleteBatchByIds(pIds);
        String msg = deleteBatchByIds ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(deleteBatchByIds ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //修改项目
    @PutMapping("/updateProject")
    public ResponseResult<?> updateProject(@RequestBody Project project) {
        Boolean updateById = projectService.updateProject(project);
        String msg = updateById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据项目id修改项目状态
    @GetMapping("/updateStatus")
    public ResponseResult<?> updateProjectStatusById(String pid, Integer status) {
        Long proId = null;
        if (StringUtils.hasText(pid)) {
            proId = Long.parseLong(pid);
        }
        Boolean updateStatusById = projectService.updateProjectStatusById(proId, status);
        String msg = updateStatusById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateStatusById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据项目id查项目信息及创建人
    @GetMapping("/{pid}")
    public ResponseResult<Project> selectByProjectId(@PathVariable Long pid) {
        Project projectById = projectService.selectByProjectId(pid);
        int code = projectById != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = projectById != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, projectById);
    }

    //分页查询所有项目或分页模糊查询、根据userId分页查询所参与的项目
    @GetMapping("/page")
    public ResponseResult<List<Project>> selectPageProject(Integer currentPage, Integer pageSize, String userId, String name, Integer status, String startTime, String endTime, String creatorId) {
        Long crId = StringUtils.hasText(creatorId) ? Long.parseLong(creatorId) : null;
        Long uId = StringUtils.hasText(userId) ? Long.parseLong(userId) : null;

        Page<Project> projectPage;
        if (null != currentPage && null != pageSize) {
            projectPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            projectPage = PageDTO.of(1, -1);
        }
        IPage<Project> projectIPage = projectService.selectPageProject(projectPage, uId, name.trim(), status, startTime.trim(), endTime.trim(), crId);
        int code = projectIPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = projectIPage.getRecords() != null ? String.valueOf(projectIPage.getTotal()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, projectIPage.getRecords());
    }

    //分页查询所有需求、任务、缺陷及模糊查询
    @GetMapping("/getAllItem")
    public ResponseResult<List<?>> selectAllItem(String title, String projectId, String iterationId) {
        Long pId = StringUtils.hasText(projectId) ? Long.parseLong(projectId) : null;
        Long iteId = StringUtils.hasText(iterationId) ? Long.parseLong(iterationId) : null;
        List<Requirement> selectAllItem = projectService.selectAllItem(title.trim(), pId, iteId);
        int code = selectAllItem != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = selectAllItem != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, selectAllItem);
    }

    //根据事项类型删除事项
    @GetMapping("/deleteItem")
    public ResponseResult<?> deleteItemById(Integer itemType, Long itemId) {
        Boolean removeItem = switch (itemType) {
            case 1 -> requirementService.deleteById(itemId);
            case 2 -> taskService.deleteById(itemId);
            case 3 -> bugService.deleteById(itemId);
            default -> false;
        };
        String msg = removeItem ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(removeItem ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //根据事项类型更改事项状态
    @GetMapping("/updateItemStatus")
    public ResponseResult<?> updateItemStatus(Integer itemType, Long itemId, Integer status) {
        Boolean updateItemPriority = switch (itemType) {
            case 1 -> requirementService.updateRequirementStatusById(itemId, status);
            case 2 -> taskService.updateTaskStatusById(itemId, status);
            case 3 -> bugService.updateBugStatusById(itemId, status);
            default -> false;
        };
        String msg = updateItemPriority ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateItemPriority ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据事项类型更改事项优先级
    @GetMapping("/updateItemPriority")
    public ResponseResult<?> updateItemPriority(Integer itemType, Long itemId, Integer priority) {
        Boolean updateItemPriority = switch (itemType) {
            case 1 -> requirementService.updateRequirementPriorityById(itemId, priority);
            case 2 -> taskService.updateTaskPriorityById(itemId, priority);
            case 3 -> bugService.updateBugPriorityById(itemId, priority);
            default -> false;
        };
        String msg = updateItemPriority ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateItemPriority ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //分页查询项目成员以及每个成员的用户组和角色及模糊查询
    @GetMapping("/selectPageProjectMembers")
    public ResponseResult<List<User>> selectPageProjectMembers(Integer currentPage, Integer pageSize, String username, String projectId) {
        Long pId = StringUtils.hasText(projectId) ? Long.parseLong(projectId) : null;
        Page<User> userPage;
        if (null != currentPage && null != pageSize) {
            userPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            userPage = PageDTO.of(1, -1);
        }
        IPage<User> iPage = projectService.selectPageProjectMembers(userPage, username.trim(), pId);
        int code = iPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = iPage.getRecords() != null ? String.valueOf(iPage.getRecords().size()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, iPage.getRecords());
    }
}
