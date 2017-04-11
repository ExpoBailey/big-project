package com.taotao.manage.service;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ItemDesc;

@Service
public class ItemDescService extends BaseService<ItemDesc>{

    /**
     * 根据商品id查询描述信息
     * @param itemId
     * @return
     */
    public ItemDesc queryItemDescByItemId(Long itemId) {
        return this.queryById(itemId);
    }

}
