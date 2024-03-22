package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Bug;
import com.ccyb.teamwork.mapper.BugMapper;
import com.ccyb.teamwork.service.IBugService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 缺陷 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class BugServiceImpl extends ServiceImpl<BugMapper, Bug> implements IBugService {

    @Autowired
    BugMapper bugMapper;

    @Override
    public Boolean addBug(Bug bug) {
        bug.setItem_type(3);
        return bugMapper.insert(bug) > 0;
    }

    @Override
    public Boolean deleteById(Long bugId) {
        return bugMapper.deleteById(bugId) > 0;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> bugIds) {
        return bugMapper.deleteBatchIds(bugIds) > 0;
    }

    @Override
    public Boolean updateBug(Bug bug) {
        Bug selectById = bugMapper.selectById(bug.getId());
        bug.setVersion(selectById.getVersion());
        bug.setModifyTime(LocalDateTime.now());
        return bugMapper.updateById(bug) > 0;
    }

    @Override
    public Bug selectBugById(Long bugId) {
        return bugMapper.selectBugById(bugId);
    }

    @Override
    public IPage<Bug> selectPageBug(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long discoveryIteId) {
        return bugMapper.selectPageBug(page,title,status,priority,creatorId,processerId,projectId,discoveryIteId);
    }

    @Override
    public Boolean updateBugStatusById(Long bugId, Integer status) {
        if ((null == bugId) || (null == status)) {
            return false;
        }
        LambdaUpdateWrapper<Bug> luw = new LambdaUpdateWrapper<>();
        luw.eq(Bug::getId, bugId).set(Bug::getStatus, status).set(Bug::getModifyTime, LocalDateTime.now());
        return bugMapper.update(null, luw) > 0;
    }

    @Override
    public Boolean updateBugPriorityById(Long bugId, Integer priority) {
        if ((null == bugId) || (null == priority)) {
            return false;
        }
        LambdaUpdateWrapper<Bug> luw = new LambdaUpdateWrapper<>();
        luw.eq(Bug::getId, bugId).set(Bug::getPriority, priority).set(Bug::getModifyTime, LocalDateTime.now());
        return bugMapper.update(null, luw) > 0;
    }

    @Override
    public List<Bug> selectRelationBugById(Long requireId) {
        if (null==requireId){
            return null;
        }
        LambdaQueryWrapper<Bug> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Bug::getRequirementId, requireId);
        return bugMapper.selectList(lqw);
    }
}
