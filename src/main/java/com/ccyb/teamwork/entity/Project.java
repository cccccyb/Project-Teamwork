package com.ccyb.teamwork.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_project")
public class Project implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId("id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 项目名
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 项目状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 开始日期
     */
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime startTime;

    /**
     * 结束时间
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

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("version")
    @Version
    private Integer version;

    /**
     * 项目成员
     */
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private List<Long> members;
}
