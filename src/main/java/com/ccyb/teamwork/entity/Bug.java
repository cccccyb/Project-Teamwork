package com.ccyb.teamwork.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 缺陷
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Data
@Accessors(chain = true)
@TableName("t_bug")
public class Bug implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 缺陷状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 严重程度
     */
    @TableField("level")
    private Integer level;

    /**
     * 重现规律
     */
    @TableField("reappear")
    private Integer reappear;

    /**
     * 创建日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 上次更新日期
     */
    @TableField("modify_time")
    private LocalDateTime modifyTime;

    /**
     * 创建人
     */
    @TableField("creator_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorId;

    /**
     * 处理人
     */
    @TableField("processer_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long processerId;

    /**
     * 所属项目
     */
    @TableField("project_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;

    /**
     * 缺陷发现迭代
     */
    @TableField("discovery_iterate_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long discoveryIterateId;

    /**
     * 规划迭代
     */
    @TableField("plan_iterate_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long planIterateId;

    /**
     * 关联需求
     */
    @TableField("requirement_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementId;

    /**
     * 缺陷类型
     */
    @TableField("bug_type_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bug_type_id;

    /**
     * 缺陷来源
     */
    @TableField("origin")
    private String origin;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("version")
    @Version
    private Integer version;
}
