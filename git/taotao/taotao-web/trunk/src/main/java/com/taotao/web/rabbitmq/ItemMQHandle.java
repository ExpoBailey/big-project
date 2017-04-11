package com.taotao.web.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;

public class ItemMQHandle {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private RedisService redisService;
    
    public static final String REDIS_KEY ="TAOTAO_WEB_ITEM_";

    /**
     * 删除redis缓存
     */
    public void deleteRedis(String msg){
        
        try {
            //获出对象信息
            JsonNode jsonNode = MAPPER.readTree(msg);
            //删除缓存
            this.redisService.del(REDIS_KEY+jsonNode.get("itemId").asLong());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
