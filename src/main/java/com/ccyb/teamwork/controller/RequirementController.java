package com.ccyb.teamwork.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ccyb.teamwork.entity.Requirement;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IRequirementService;
import com.ccyb.teamwork.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 需求 前端控制器
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/requirement")
public class RequirementController {
    @Autowired
    IRequirementService requirementService;

    //添加需求
    @PostMapping("/addRequirement")
    public ResponseResult<?> addRequirement(@RequestBody Requirement requirement) {
        Boolean addRequirement = requirementService.addRequirement(requirement);
        String msg = addRequirement ? "" : "数据添加失败，请重试！";
        return ResponseResult.build(addRequirement ? ResponseCode.DATABASE_SAVE_OK : ResponseCode.DATABASE_SAVE_ERROR, msg, null);
    }

    //删除需求
    @DeleteMapping("/{reqId}")
    public ResponseResult<?> deleteByRequirementId(@PathVariable Long reqId) {
        Boolean removeById = requirementService.deleteById(reqId);
        String msg = removeById ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(removeById ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //批量删除需求
    @PostMapping("/batch")
    public ResponseResult<?> deleteBatchByIds(@RequestBody List<String> requirementIds) {
        List<Long> reqIds = WebUtil.convertStringToLong(requirementIds);
        Boolean deleteBatchByIds = requirementService.deleteBatchByIds(reqIds);
        String msg = deleteBatchByIds ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(deleteBatchByIds ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //修改需求
    @PutMapping("/updateRequirement")
    public ResponseResult<?> updateRequirement(@RequestBody Requirement requirement) {
        System.out.println("666:"+requirement.toString());
        Boolean updateById = requirementService.updateRequirement(requirement);
        String msg = updateById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据需求id修改需求状态
    @GetMapping("/updateStatus")
    public ResponseResult<?> updateRequirementStatusById(String reqId,Integer status) {
        Long rId = null;
        if (StringUtils.hasText(reqId)) {
            rId = Long.parseLong(reqId);
        }
        Boolean updateStatusById = requirementService.updateRequirementStatusById(rId,status);
        String msg = updateStatusById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateStatusById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据需求id修改需求优先级
    @GetMapping("/updatePriority")
    public ResponseResult<?> updateRequirementPriorityById(String reqId,Integer priority) {
        Long rId = null;
        if (StringUtils.hasText(reqId)) {
            rId = Long.parseLong(reqId);
        }
        Boolean updatePriorityById = requirementService.updateRequirementPriorityById(rId,priority);
        String msg = updatePriorityById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updatePriorityById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据需求id查需求信息及负责人
    @GetMapping("/{reqId}")
    public ResponseResult<Requirement> selectByRequirementId(@PathVariable Long reqId) {
        Requirement requirement = requirementService.selectRequirementById(reqId);
        int code = requirement != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = requirement != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, requirement);
    }

    //分页查询所有需求或分页模糊查询
    @GetMapping("/page")
    public ResponseResult<List<Requirement>> selectPageRequirement(Integer currentPage, Integer pageSize,  String title, Integer status, Integer priority, String creatorId, String processerId, String projectId, String iterationId) {
        Long creId = StringUtils.hasText(creatorId)?Long.parseLong(creatorId):null;
        Long proId = StringUtils.hasText(processerId)?Long.parseLong(processerId):null;
        Long pId = StringUtils.hasText(projectId)?Long.parseLong(projectId):null;
        Long iteId = StringUtils.hasText(iterationId)?Long.parseLong(iterationId):null;

        Page<Requirement> requirementPage;
        if (null != currentPage && null != pageSize) {
            requirementPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            requirementPage = PageDTO.of(1, -1);
        }
        IPage<Requirement> requirementIPage = requirementService.selectPageRequirement(requirementPage, title.trim(), status,priority,creId,proId,pId,iteId);
        int code = requirementIPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = requirementIPage.getRecords() != null ? String.valueOf(requirementIPage.getTotal()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, requirementIPage.getRecords());
    }

    //根据项目id查所有需求
    @GetMapping("/getAllRequirement")
    public ResponseResult<List<Requirement>> getAllRequirement(Long projectId) {
        List<Requirement> allRequirement = requirementService.getAllRequirement(projectId);
        int code = allRequirement != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = allRequirement != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, allRequirement);
    }

}
