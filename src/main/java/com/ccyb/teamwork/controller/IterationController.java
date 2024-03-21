package com.ccyb.teamwork.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ccyb.teamwork.entity.Iteration;
import com.ccyb.teamwork.entity.Project;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IIterationService;
import com.ccyb.teamwork.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 迭代 前端控制器
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/iteration")
public class IterationController {
    @Autowired
    IIterationService iIterationService;

    //添加迭代
    @PostMapping("/addIteration")
    public ResponseResult<?> addIteration(@RequestBody Iteration iteration) {
        Boolean addIteration = iIterationService.addIteration(iteration);
        String msg = addIteration ? "" : "数据添加失败，请重试！";
        return ResponseResult.build(addIteration ? ResponseCode.DATABASE_SAVE_OK : ResponseCode.DATABASE_SAVE_ERROR, msg, null);
    }

    //删除迭代
    @DeleteMapping("/{iteId}")
    public ResponseResult<?> deleteByIterationId(@PathVariable Long iteId) {
        Boolean removeById = iIterationService.deleteById(iteId);
        String msg = removeById ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(removeById ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //批量删除迭代
    @PostMapping("/batch")
    public ResponseResult<?> deleteBatchByIds(@RequestBody List<String> iterationIds) {
        List<Long> iteIds = WebUtil.convertStringToLong(iterationIds);
        Boolean deleteBatchByIds = iIterationService.deleteBatchByIds(iteIds);
        String msg = deleteBatchByIds ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(deleteBatchByIds ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //修改迭代
    @PutMapping("/updateIteration")
    public ResponseResult<?> updateIteration(@RequestBody Iteration iteration) {
        Boolean updateById = iIterationService.updateIteration(iteration);
        String msg = updateById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据迭代id修改迭代状态
    @GetMapping("/updateStatus")
    public ResponseResult<?> updateIterationStatusById(String iteId,Integer status) {
        Long iId = null;
        if (StringUtils.hasText(iteId)) {
            iId = Long.parseLong(iteId);
        }
        Boolean updateStatusById = iIterationService.updateIterationStatusById(iId,status);
        String msg = updateStatusById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateStatusById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据项目id查迭代信息及负责人
    @GetMapping("/{iteId}")
    public ResponseResult<Iteration> selectByIterationId(@PathVariable Long iteId) {
        Iteration iteration = iIterationService.selectByIterationId(iteId);
        int code = iteration != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = iteration != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, iteration);
    }

    //分页查询所有迭代或分页模糊查询
    @GetMapping("/page")
    public ResponseResult<List<Iteration>> selectPageIteration(Integer currentPage, Integer pageSize, String name, Integer status, String directorId, String projectId) {
        Long dirId = StringUtils.hasText(directorId)?Long.parseLong(directorId):null;
        Long pId = StringUtils.hasText(projectId)?Long.parseLong(projectId):null;

        Page<Project> projectPage;
        if (null != currentPage && null != pageSize) {
            projectPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            projectPage = PageDTO.of(1, -1);
        }
        IPage<Iteration> iterationIPage = iIterationService.selectPageIteration(projectPage, name.trim(), status, dirId, pId);
        int code = iterationIPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = iterationIPage.getRecords() != null ? String.valueOf(iterationIPage.getTotal()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, iterationIPage.getRecords());
    }

}
