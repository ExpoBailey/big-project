package com.taoao.search.rabbitmq;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemContentService;
import com.taotao.search.service.ItemService;

public class ItemMQHandle {

    @Autowired
    private HttpSolrServer httpSolrServer;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private ItemContentService itemContentService;
    
    public void updateIndex(String msg){
        
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            String type = jsonNode.get("type").asText();
            String itemId = jsonNode.get("itemId").asText();
            
            if (StringUtils.equals(type, "delete")) {
                this.httpSolrServer.deleteById(itemId);
            }else{
               Item item = this.itemContentService.queryItemByItemId(itemId);
               this.httpSolrServer.addBean(item);
            }
            
            //提交
            this.httpSolrServer.commit();
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
}
