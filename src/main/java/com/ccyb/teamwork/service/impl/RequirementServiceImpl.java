package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Requirement;
import com.ccyb.teamwork.mapper.RequirementMapper;
import com.ccyb.teamwork.service.IRequirementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 需求 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class RequirementServiceImpl extends ServiceImpl<RequirementMapper, Requirement> implements IRequirementService {
    @Autowired
    RequirementMapper requirementMapper;

    @Override
    public Boolean addRequirement(Requirement requirement) {
        requirement.setItem_type(1);
        return requirementMapper.insert(requirement) > 0;
    }

    @Override
    public Boolean deleteById(Long reqId) {
        return requirementMapper.deleteById(reqId) > 0;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> reqIds) {
        return requirementMapper.deleteBatchIds(reqIds) > 0;
    }

    @Override
    public Boolean updateRequirement(Requirement requirement) {
        Requirement selectById = requirementMapper.selectById(requirement.getId());
        requirement.setVersion(selectById.getVersion());
        requirement.setModifyTime(LocalDateTime.now());
        return requirementMapper.updateById(requirement) > 0;
    }

    @Override
    public Requirement selectRequirementById(Long reqId) {
        return requirementMapper.selectRequirementById(reqId);
    }

    @Override
    public IPage<Requirement> selectPageRequirement(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long iterationId) {
        return requirementMapper.selectPageRequirement(page,title,status,priority,creatorId,processerId,projectId,iterationId);
    }

    @Override
    public Boolean updateRequirementStatusById(Long reqId, Integer status) {
        if ((null == reqId) || (null == status)) {
            return false;
        }
        LambdaUpdateWrapper<Requirement> luw = new LambdaUpdateWrapper<>();
        luw.eq(Requirement::getId, reqId).set(Requirement::getStatus, status).set(Requirement::getModifyTime, LocalDateTime.now());
        return requirementMapper.update(null, luw) > 0;
    }

    @Override
    public Boolean updateRequirementPriorityById(Long reqId, Integer priority) {
        if ((null == reqId) || (null == priority)) {
            return false;
        }
        LambdaUpdateWrapper<Requirement> luw = new LambdaUpdateWrapper<>();
        luw.eq(Requirement::getId, reqId).set(Requirement::getPriority, priority).set(Requirement::getModifyTime, LocalDateTime.now());
        return requirementMapper.update(null, luw) > 0;
    }
}
