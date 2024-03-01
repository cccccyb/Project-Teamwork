package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.Bug;
import com.ccyb.teamwork.mapper.BugMapper;
import com.ccyb.teamwork.service.IBugService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 缺陷 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class BugServiceImpl extends ServiceImpl<BugMapper, Bug> implements IBugService {

}
