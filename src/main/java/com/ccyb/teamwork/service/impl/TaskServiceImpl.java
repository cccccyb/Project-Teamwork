package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ccyb.teamwork.entity.Task;
import com.ccyb.teamwork.mapper.TaskMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事项 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements IService<Task> {

}
