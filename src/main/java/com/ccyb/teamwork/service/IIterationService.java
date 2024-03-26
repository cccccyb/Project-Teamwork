package com.ccyb.teamwork.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Iteration;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 迭代 服务类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
public interface IIterationService extends IService<Iteration> {
    Boolean addIteration(Iteration iteration);

    Boolean deleteById(Long iteId);

    Boolean deleteBatchByIds(List<Long> iteIds);

    Boolean updateIteration(Iteration iteration);

    Iteration selectByIterationId(Long iteId);

    IPage<Iteration> selectPageIteration(IPage<?> page, String name, Integer status, Long directorId, Long projectId);

    Boolean updateIterationStatusById(Long iteId,Integer status);

    List<Iteration> getAllIteration(Long projectId);

    List<Iteration> selectIterationLimit();

}
