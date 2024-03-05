package com.ccyb.teamwork.controller;

import com.ccyb.teamwork.entity.Project;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Long> pIds = projectIds.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
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

    //根据项目id查项目信息及创建人
    @GetMapping("/{pid}")
    public ResponseResult<Project> selectByNoticeId(@PathVariable Long pid) {
        Project projectById = projectService.selectByProjectId(pid);
        int code = projectById != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = projectById != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, projectById);
    }

}
