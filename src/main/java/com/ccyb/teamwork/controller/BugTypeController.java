package com.ccyb.teamwork.controller;

import com.ccyb.teamwork.entity.Bug;
import com.ccyb.teamwork.entity.BugType;
import com.ccyb.teamwork.entity.common.ResponseCode;
import com.ccyb.teamwork.entity.common.ResponseResult;
import com.ccyb.teamwork.service.IBugTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 事项类型 前端控制器
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/bugType")
public class BugTypeController {
    @Autowired
    IBugTypeService bugTypeService;

    //获得所有缺陷类型
    @GetMapping("/getAllBugType")
    public ResponseResult<List<BugType>> getAllBug() {
        List<BugType> allBugType = bugTypeService.getAllBugType();
        int code = allBugType != null ? ResponseCode.DATABASE_SELECT_OK : ResponseCode.DATABASE_SELECT_ERROR;
        String msg = allBugType != null ? "" : "数据查询失败，请重试！";
        return ResponseResult.build(code, msg, allBugType);
    }

}
