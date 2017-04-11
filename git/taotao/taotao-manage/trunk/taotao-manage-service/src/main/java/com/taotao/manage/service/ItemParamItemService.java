package com.taotao.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemParamItemService extends BaseService<ItemParamItem>{
    
    @Autowired
    private ItemParamItemMapper itemParamItemMapper;

    /**
     * 根据商品id查询规格参数数据
     * @param itemId
     * @return
     */
    public ItemParamItem queryItemParamItemByItemId(Long itemId) {
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(itemId);
        return  this.itemParamItemMapper.selectOne(itemParamItem);
    }

}
