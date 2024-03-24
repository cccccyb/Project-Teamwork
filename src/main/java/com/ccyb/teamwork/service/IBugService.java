package com.ccyb.teamwork.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Bug;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 缺陷 服务类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
public interface IBugService extends IService<Bug> {
    Boolean addBug(Bug bug);

    Boolean deleteById(Long bugId);

    Boolean deleteBatchByIds(List<Long> bugIds);

    Boolean updateBug(Bug bug);
    Bug selectBugById(Long bugId);

    IPage<Bug> selectPageBug(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long discoveryIteId);

    Boolean updateBugStatusById(Long bugId,Integer status);

    Boolean updateBugPriorityById(Long bugId,Integer priority);

    List<Bug> selectRelationBugById(Long requireId);

    List<Bug> getAllBug(Long projectId, Long requireId);

    Boolean addRelationBugById(Long requireId, List<Long> bugIds);

}
