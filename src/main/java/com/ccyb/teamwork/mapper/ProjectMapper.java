package com.ccyb.teamwork.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 项目 Mapper 接口
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    Project selectProjectById(Long pid);

    IPage<Project> selectPageProject(IPage<?> page, String name, Integer status, LocalDateTime startTime, LocalDateTime endTime, Long creatorId);

}
