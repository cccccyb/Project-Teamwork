package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Iteration;
import com.ccyb.teamwork.mapper.IterationMapper;
import com.ccyb.teamwork.service.IIterationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 迭代 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class IterationServiceImpl extends ServiceImpl<IterationMapper, Iteration> implements IIterationService {

    @Autowired
    IterationMapper iterationMapper;

    @Override
    public Boolean addIteration(Iteration iteration) {
        return iterationMapper.insert(iteration) > 0;
    }

    @Override
    public Boolean deleteById(Long iteId) {
        return iterationMapper.deleteById(iteId) > 0;
    }

    @Override
    public Boolean deleteBatchByIds(List<Long> iteIds) {
        return iterationMapper.deleteBatchIds(iteIds) > 0;
    }

    @Override
    public Boolean updateIteration(Iteration iteration) {
        Iteration selectById = iterationMapper.selectById(iteration.getId());
        iteration.setVersion(selectById.getVersion());
        return iterationMapper.updateById(iteration) > 0;
    }

    @Override
    public Iteration selectByIterationId(Long iteId) {
        return iterationMapper.selectIterationById(iteId);
    }

    @Override
    public IPage<Iteration> selectPageIteration(IPage<?> page, String name, Integer status, Long directorId, Long projectId) {
        return iterationMapper.selectPageIteration(page,name, status, directorId,projectId);
    }

    @Override
    public Boolean updateIterationStatusById(Long iteId, Integer status) {
        if ((null == iteId) || (null == status)) {
            return false;
        }
        LambdaUpdateWrapper<Iteration> luw = new LambdaUpdateWrapper<>();
        luw.eq(Iteration::getId, iteId).set(Iteration::getStatus, status);
        return iterationMapper.update(null, luw) > 0;
    }

    @Override
    public List<Iteration> getAllIteration(Long projectId) {
        if (null==projectId){
            return null;
        }
        LambdaQueryWrapper<Iteration> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Iteration::getProjectId, projectId);
        return iterationMapper.selectList(lqw);
    }

    @Override
    public List<Iteration> selectIterationLimit() {
        return iterationMapper.selectIterationLimit();
    }
}
