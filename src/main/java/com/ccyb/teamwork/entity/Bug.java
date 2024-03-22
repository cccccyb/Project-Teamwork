package com.ccyb.teamwork.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime createTime;

    /**
     * 上次更新日期
     */
    @TableField("modify_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime modifyTime;

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
     * 缺陷发现迭代id
     */
    @TableField("discovery_iterate_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long discoveryIterateId;

    /**
     * 缺陷发现迭代
     */
    @TableField(exist = false)
    private Iteration discoveryIterate;

    /**
     * 关联需求id
     */
    @TableField("requirement_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requirementId;

    /**
     * 关联需求
     */
    @TableField(exist = false)
    private Requirement requirement;

    /**
     * 缺陷类型id
     */
    @TableField("bug_type_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bug_type_id;

    /**
     * 缺陷类型
     */
    @TableField(exist = false)
    private BugType bugType;

    /**
     * 缺陷来源
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
