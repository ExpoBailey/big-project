package com.taotao.manage.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.ItemParam;

@Service
public class ItemParamService extends BaseService<ItemParam>{

    /**
     * 根据模板id查询模板数据
     * @param id
     * @return
     */
    public ItemParam queryItemParamById(Long id) {
       return this.queryById(id);
    }

    /**
     * 新增模板
     */
    public void saveItemParam(Long itemCatId, String paramData) {
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(itemCatId);
        itemParam.setParamData(paramData);
        itemParam.setCreated(new Date());
        itemParam.setUpdated(itemParam.getCreated());
        this.saveSelective(itemParam);
        
    }

    /**
     * 分页查询规格参数模板
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<ItemParam> queryItemParamByPage(Integer page, Integer rows) {
        PageInfo<ItemParam> pageInfo = this.queryByPageWhere(null, page, rows);
        return pageInfo;
    }

    /**
     * 根据类目id查询模板数据
     * @param itemCatId
     * @return
     */
    public ItemParam queryItemParamByItemCatId(Long itemCatId) {
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(itemCatId);
        ItemParam itemParam2 = this.queryOne(itemParam);
        return itemParam2;
    }

}
