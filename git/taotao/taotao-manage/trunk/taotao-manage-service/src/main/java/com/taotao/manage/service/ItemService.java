package com.taotao.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ItemDescMapper;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.mapper.ItemParamMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {
    
    @Autowired
    private ItemDescMapper itemDescMapper;
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private ItemParamItemMapper itemParamItemMapper;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 添加商品
     * 
     * @param item
     * @return
     */
    public void saveItem(Item item,String desc,String itemParams) {
        try {
            //判断是否开启debug日志
            if (LOGGER.isDebugEnabled()) {
                //输入参数输出
                LOGGER.debug("执行新添商品，item={},itemdesc={}", item ,desc);
            }
            //事务问题
            item.setId(null);
            item.setCreated(new Date());
            item.setUpdated(item.getCreated());
            this.saveSelective(item);
            
            ItemDesc itemDesc = new ItemDesc();
            itemDesc.setItemId(item.getId());
            itemDesc.setItemDesc(desc);
            itemDesc.setCreated(new Date());
            itemDesc.setUpdated(itemDesc.getCreated());
            this.itemDescMapper.insertSelective(itemDesc);
            
            ItemParamItem itemParam = new ItemParamItem();
            itemParam.setId(null);
            itemParam.setItemId(item.getId());
            itemParam.setParamData(itemParams);
            itemParam.setCreated(new Date());
            itemParam.setUpdated(itemParam.getCreated());
            
            this.itemParamItemMapper.insertSelective(itemParam);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("新添商品成功，item={},itemdesc={}", item ,desc);
            }
            
            sendMQ("save",item.getId());
            
        } catch (Exception e) {
            LOGGER.error("新添商品失败！item="+item,e);
            e.printStackTrace();
        }

    }

    /**
     * 发送消息的方法
     * @param string
     * @param id
     */
    private void sendMQ(String type, Long itemId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", type);
            map.put("itemId", itemId);
            rabbitTemplate.convertAndSend("item."+type, MAPPER.writeValueAsString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 商品列表管理功能
     */
    public PageInfo<Item> queryByPage(Integer page, Integer rows) {
        Example example=new Example(Item.class);
        example.setOrderByClause("updated DESC");
        PageHelper.startPage(page, rows);
        List<Item> list = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(list);
        return pageInfo;
    }

    /**
     * 商品编辑功能实现
     */
    public void updateItem(Item item, String desc) {
        item.setUpdated(new Date());
        this.updateSelective(item);
        
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        this.itemDescMapper.updateByPrimaryKeySelective(itemDesc);
        //发送消息
        sendMQ("update",item.getId());
    }

}
