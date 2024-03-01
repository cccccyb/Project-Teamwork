package com.ccyb.teamwork.mapper;

import com.ccyb.teamwork.entity.UserGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 中间表-用户-用户组 Mapper 接口
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroup> {

}
