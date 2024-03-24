package com.ccyb.teamwork.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccyb.teamwork.entity.BugType;

import java.util.List;

/**
 * <p>
 * 缺陷 服务类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
public interface IBugTypeService extends IService<BugType> {
    List<BugType> getAllBugType();

}
