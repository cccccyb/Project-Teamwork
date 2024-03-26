package com.ccyb.teamwork.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    Task selectTaskById(Long tId);

    IPage<Task> selectPageTask(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long iterationId);

    List<Task> selectTaskLimit();
}
