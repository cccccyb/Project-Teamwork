package com.ccyb.teamwork.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ccyb.teamwork.entity.BugType;
import com.ccyb.teamwork.mapper.BugTypeMapper;
import com.ccyb.teamwork.service.IBugTypeService;
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
public class BugTypeServiceImpl extends ServiceImpl<BugTypeMapper, BugType> implements IBugTypeService {

}
