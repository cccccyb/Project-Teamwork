package com.ccyb.teamwork.mapper;

import com.ccyb.teamwork.entity.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
