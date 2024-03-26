package com.ccyb.teamwork.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Iteration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 * 迭代 Mapper 接口
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Mapper
public interface IterationMapper extends BaseMapper<Iteration> {
    Iteration selectIterationById(Long iteId);

    IPage<Iteration> selectPageIteration(IPage<?> page, String name, Integer status, Long directorId, Long projectId);

    List<Iteration> selectIterationLimit();

}
