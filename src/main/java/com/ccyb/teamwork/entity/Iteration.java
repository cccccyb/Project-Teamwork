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
 * 迭代
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_iteration")
public class Iteration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 迭代名称
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 迭代状态
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
     * 结束日期
     */
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime endTime;

    /**
     * 负责人
     */
    @TableField("director_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long directorId;

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
