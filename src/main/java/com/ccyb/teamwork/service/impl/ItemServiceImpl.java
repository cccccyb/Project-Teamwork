package com.ccyb.teamwork.service.impl;

import com.ccyb.teamwork.entity.Item;
import com.ccyb.teamwork.mapper.ItemMapper;
import com.ccyb.teamwork.service.IItemService;
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
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

}
