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
 * 中间表-需求-迭代
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_requirement_iteration")
public class RequirementIteration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 需求
     */
    @TableField("requirement_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementId;

    /**
     * 迭代
     */
    @TableField("iteration_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long iterationId;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("version")
    @Version
    private Integer version;
}
