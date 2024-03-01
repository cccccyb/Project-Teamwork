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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 事项
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_item")
public class Item implements Serializable {

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
     * 事项状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 置顶
     */
    @TableField("top")
    private Integer top;

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
     * 结束日期
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 事项类型
     */
    @TableField("item_type_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long itemTypeId;

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

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("version")
    @Version
    private Integer version;
}
