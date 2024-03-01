package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.ItemType;
import com.ccyb.teamwork.mapper.ItemTypeMapper;
import com.ccyb.teamwork.service.IItemTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事项类型 服务实现类
 * </p>
 *
 * @author cccccyb
 * @since 2024-03-01
 */
@Service
public class ItemTypeServiceImpl extends ServiceImpl<ItemTypeMapper, ItemType> implements IItemTypeService {

}
