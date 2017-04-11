package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.ItemDesc;
import com.taotao.web.pojo.ItemParamItem;

@Service
public class ItemService {
    
    @Autowired
    private ApiService apiService;
    
    @Autowired
    private RedisService redisService;
    
    @Value("${MANAGE_TAOTAO_URL}")
    private String MANAGE_TAOTAO_URL;
    
    public static final String REDIS_ITEM = "TAOTAO_WEB_ITEM_";
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryItemByItemId(Long itemId) {
        String key = REDIS_ITEM + itemId;
        try {
            // 从缓存中命中
            String cacheJson = this.redisService.get(key);
            if (StringUtils.equals("404", cacheJson)) {
                //非法请求，直接返回null
                return null;
            }
            if (StringUtils.isNotEmpty(cacheJson)) {
                return MAPPER.readValue(cacheJson, Item.class);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url =MANAGE_TAOTAO_URL+ "/rest/item/"+itemId;
        
        try {
            String jsonData = this.apiService.doGet(url);
            if (null == jsonData) {

                try {
                    //非法请求的数据，缓存一小时
                    // 写入到缓存中
                    this.redisService.setExpire(key, "404", 3600);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 响应是404、500
                return null;
            }

            try {
                // 写入到缓存中
                this.redisService.setExpire(key, jsonData, 60 * 60 * 24);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MAPPER.readValue(jsonData, Item.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryItemDescByItemId(Long itemId) {
        String url = MANAGE_TAOTAO_URL+"/rest/item/desc/"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (jsonData == null) {
                return null;
            }
            return MAPPER.readValue(jsonData, ItemDesc.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryItemParamToHtml(Long itemId) {
        String url = MANAGE_TAOTAO_URL+"/rest/item/param/item/query/"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if (jsonData.isEmpty()) {
                return null;
            }
            ItemParamItem itemParamItem = MAPPER.readValue(jsonData, ItemParamItem.class);
            if (itemParamItem !=null) {
                String paramJosn = itemParamItem.getParamData();
                //解释json
                JsonNode jsonNode = MAPPER.readTree(paramJosn);
                StringBuilder sb = new StringBuilder();
                sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"> <tbody>");
                for (JsonNode node : jsonNode) {
                    String group = node.get("group").asText();
                    sb.append("<tr> <th class=\"tdTitle\" colspan=\"2\">"+group+"</th> </tr>");
                    JsonNode jsonNode2 = node.get("params");
                    for (JsonNode jsonNode3 : jsonNode2) {
                        sb.append("<tr><td class=\"tdTitle\">"+jsonNode3.get("k").asText()+"</td><td>"+jsonNode3.get("v").asText()+"</td></tr>");
                    }
                }
                sb.append("</tbody></table>");

                return sb.toString();
            }
         
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
