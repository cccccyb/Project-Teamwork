package com.ccyb.teamwork.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 中间表-用户-用户组
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_group")
public class UserGroup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 用户组
     */
    @TableField("group_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long groupId;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("version")
    @Version
    private Integer version;
}
