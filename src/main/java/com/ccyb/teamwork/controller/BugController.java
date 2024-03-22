package com.ccyb.teamwork.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ccyb.teamwork.entity.Bug;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IBugService;
import com.ccyb.teamwork.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 缺陷 前端控制器
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/bug")
public class BugController {
    @Autowired
    IBugService bugService;

    //添加缺陷
    @PostMapping("/addBug")
    public ResponseResult<?> addBug(@RequestBody Bug bug) {
        Boolean addBug = bugService.addBug(bug);
        String msg = addBug ? "" : "数据添加失败，请重试！";
        return ResponseResult.build(addBug ? ResponseCode.DATABASE_SAVE_OK : ResponseCode.DATABASE_SAVE_ERROR, msg, null);
    }

    //删除缺陷
    @DeleteMapping("/{bugId}")
    public ResponseResult<?> deleteByBugId(@PathVariable Long bugId) {
        Boolean removeById = bugService.deleteById(bugId);
        String msg = removeById ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(removeById ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //批量删除缺陷
    @PostMapping("/batch")
    public ResponseResult<?> deleteBatchByIds(@RequestBody List<String> bugIds) {
        List<Long> bIds = WebUtil.convertStringToLong(bugIds);
        Boolean deleteBatchByIds = bugService.deleteBatchByIds(bIds);
        String msg = deleteBatchByIds ? "" : "数据删除失败，请重试！";
        return ResponseResult.build(deleteBatchByIds ? ResponseCode.DATABASE_DELETE_OK : ResponseCode.DATABASE_DELETE_ERROR, msg, null);
    }

    //修改缺陷
    @PutMapping("/updateBug")
    public ResponseResult<?> updateBug(@RequestBody Bug bug) {
        Boolean updateById = bugService.updateBug(bug);
        String msg = updateById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据缺陷id修改缺陷状态
    @GetMapping("/updateStatus")
    public ResponseResult<?> updateBugStatusById(String bugId,Integer status) {
        Long bId = null;
        if (StringUtils.hasText(bugId)) {
            bId = Long.parseLong(bugId);
        }
        Boolean updateStatusById = bugService.updateBugStatusById(bId,status);
        String msg = updateStatusById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updateStatusById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据缺陷id修改缺陷优先级
    @GetMapping("/updatePriority")
    public ResponseResult<?> updateBugPriorityById(String bugId,Integer priority) {
        Long bId = null;
        if (StringUtils.hasText(bugId)) {
            bId = Long.parseLong(bugId);
        }
        Boolean updatePriorityById = bugService.updateBugPriorityById(bId,priority);
        String msg = updatePriorityById ? "" : "数据修改失败，请重试！";
        return ResponseResult.build(updatePriorityById ? ResponseCode.DATABASE_UPDATE_OK : ResponseCode.DATABASE_UPDATE_ERROR, msg, null);
    }

    //根据缺陷id查缺陷信息及负责人
    @GetMapping("/{bugId}")
    public ResponseResult<Bug> selectByBugId(@PathVariable Long bugId) {
        Bug bug = bugService.selectBugById(bugId);
        int code = bug != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = bug != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, bug);
    }

    //分页查询所有缺陷或分页模糊查询
    @GetMapping("/page")
    public ResponseResult<List<Bug>> selectPageBug(Integer currentPage, Integer pageSize,  String title, Integer status, Integer priority, String creatorId, String processerId, String projecbugId, String discoveryIteId) {
        Long creId = StringUtils.hasText(creatorId)?Long.parseLong(creatorId):null;
        Long proId = StringUtils.hasText(processerId)?Long.parseLong(processerId):null;
        Long pId = StringUtils.hasText(projecbugId)?Long.parseLong(projecbugId):null;
        Long iteId = StringUtils.hasText(discoveryIteId)?Long.parseLong(discoveryIteId):null;

        Page<Bug> bugtPage;
        if (null != currentPage && null != pageSize) {
            bugtPage = PageDTO.of(currentPage, pageSize);
        } else {
            // 不进行分页
            bugtPage = PageDTO.of(1, -1);
        }
        IPage<Bug> bugIPage = bugService.selectPageBug(bugtPage, title.trim(), status,priority,creId,proId,pId,iteId);
        int code = bugIPage.getRecords() != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = bugIPage.getRecords() != null ? String.valueOf(bugIPage.getTotal()) : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, bugIPage.getRecords());
    }

    //根据需求id查所关联的缺陷
    @GetMapping("/{requireId}")
    public ResponseResult<List<Bug>> selectRelationBugById(@PathVariable Long requireId) {
        List<Bug> relationBug = bugService.selectRelationBugById(requireId);
        int code = relationBug != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = relationBug != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, relationBug);
    }

}
