package com.ccyb.teamwork.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Requirement;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 需求 服务类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
public interface IRequirementService extends IService<Requirement> {
    Boolean addRequirement(Requirement requirement);

    Boolean deleteById(Long reqId);

    Boolean deleteBatchByIds(List<Long> reqIds);

    Boolean updateRequirement(Requirement requirement);
    Requirement selectRequirementById(Long reqId);

    IPage<Requirement> selectPageRequirement(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long iterationId);

    Boolean updateRequirementStatusById(Long reqId,Integer status);

    Boolean updateRequirementPriorityById(Long reqId,Integer priority);

    List<Requirement> getAllRequirement(Long projectId);


}
