package com.ccyb.teamwork.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ccyb.teamwork.entity.Project;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IProjectService;
import com.ccyb.teamwork.service.IProjectUserService;
import com.ccyb.teamwork.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
    @Autowired
    IProjectUserService projectUserService;

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

    //根据项目id修改项目状态
    @GetMapping("/updateStatus")
    public ResponseResult<?> updateProjectStatusById(String pid,Integer status) {
        Long proId = null;
        if (StringUtils.hasText(pid)) {
            proId = Long.parseLong(pid);
        }
        Boolean updateStatusById = projectService.updateProjectStatusById(proId,status);
        String msg = updateStatusById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateStatusById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据项目id查项目信息及创建人
    @GetMapping("/{pid}")
    public ResponseResult<Project> selectByNoticeId(@PathVariable Long pid) {
        Project projectById = projectService.selectByProjectId(pid);
        int code = projectById != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = projectById != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, projectById);
    }

    //分页查询所有项目或分页模糊查询、根据userId分页查询所参与的项目
    @GetMapping("/page")
    public ResponseResult<List<Project>> selectPageNotice(Integer currentPage, Integer pageSize,String userId, String name, Integer status, String startTime, String endTime, String creatorId) {
        Long uId = null, crId = null;
        if (StringUtils.hasText(userId)) {
            uId = Long.parseLong(userId);
        }
        if (StringUtils.hasText(creatorId)) {
            crId = Long.parseLong(creatorId);
        }
        Page<Project> projectPage;
        if (null != currentPage && null != pageSize) {
            projectPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            projectPage = PageDTO.of(1, -1);
        }
        IPage<Project> projectIPage = projectService.selectPageProject(projectPage, uId,name.trim(), status, startTime.trim(), endTime.trim(), crId);
        int code = projectIPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = projectIPage.getRecords() != null ? String.valueOf(projectIPage.getTotal()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, projectIPage.getRecords());
    }

    //根据userId分页查询所参与的项目
//    @GetMapping("/selectPageMyAttend")
//    public ResponseResult<List<Project>> selectPageMyAttend(Integer currentPage, Integer pageSize, String userId,String name, Integer status, String startTime, String endTime, String creatorId) {
//        Long uId = null, crId = null;
//        if (StringUtils.hasText(userId)) {
//            uId = Long.parseLong(userId);
//        }
//        if (StringUtils.hasText(creatorId)) {
//            crId = Long.parseLong(creatorId);
//        }
//        Page<Project> projectPage;
//        if (null != currentPage && null != pageSize) {
//            projectPage = PageDTO.of(currentPage, pageSize);
//        } else {
//            // 不进行分页
//            projectPage = PageDTO.of(1, -1);
//        }
//        IPage<Project> projectsMyAttend = projectUserService.selectPageMyAttend(projectPage, uId,name.trim(), status, startTime.trim(), endTime.trim(), crId);
//        int code = projectsMyAttend.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
//        String msg = projectsMyAttend.getRecords() != null ? String.valueOf(projectsMyAttend.getTotal()) : "数据查询失败，请重试！";
//        return ResponseResult.build(code, msg, projectsMyAttend.getRecords());
//    }

}
