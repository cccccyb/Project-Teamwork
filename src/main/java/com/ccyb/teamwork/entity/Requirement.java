package com.ccyb.teamwork.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 需求
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_requirement")
public class Requirement implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
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
     * 需求状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 创建日期
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime createTime;

    /**
     * 上次更新日期
     */
    @TableField("modify_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime modifyTime;

    /**
     * 截止日期
     */
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime endTime;

    /**
     * 创建人id
     */
    @TableField("creator_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorId;

    /**
     * 创建人
     */
    @TableField(exist = false)
    private User creator;

    /**
     * 处理人id
     */
    @TableField("processer_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long processerId;

    /**
     * 处理人
     */
    @TableField(exist = false)
    private User processer;

    /**
     * 所属项目id
     */
    @TableField("project_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;

    /**
     * 所属项目
     */
    @TableField(exist = false)
    private Project project;

    /**
     * 所属迭代id
     */
    @TableField("iteration_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long iteration_id;

    /**
     * 所属迭代
     */
    @TableField(exist = false)
    private Iteration iteration;

    /**
     * 需求来源
     */
    @TableField("origin")
    private String origin;

    /**
     * 事项类型
     */
    @TableField("item_type")
    private Integer item_type;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("version")
    @Version
    private Integer version;
}
