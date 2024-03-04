package com.ccyb.teamwork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccyb.teamwork.entity.Task;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 事项 Mapper 接口
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

}