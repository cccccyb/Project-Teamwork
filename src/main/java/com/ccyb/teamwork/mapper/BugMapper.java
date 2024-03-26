package com.ccyb.teamwork.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccyb.teamwork.entity.Bug;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 缺陷 Mapper 接口
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Mapper
public interface BugMapper extends BaseMapper<Bug> {

    Bug selectBugById(Long bugId);

    IPage<Bug> selectPageBug(IPage<?> page, String title, Integer status, Integer priority, Long creatorId, Long processerId, Long projectId, Long discoveryIteId);

    List<Bug> selectBugLimit();

}
