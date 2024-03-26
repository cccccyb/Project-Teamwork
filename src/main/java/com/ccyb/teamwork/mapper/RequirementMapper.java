package com.ccyb.teamwork.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Requirement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 需求 Mapper 接口
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Mapper
public interface RequirementMapper extends BaseMapper<Requirement> {
    Requirement selectRequirementById(Long reqId);

    IPage<Requirement> selectPageRequirement(IPage<?> page, String title, Integer status,Integer priority, Long creatorId, Long processerId,Long projectId,Long iterationId);

    List<Requirement> selectRequirementLimit();

}
