package com.taotao.search.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.search.pojo.Item;

@Service
public class ItemContentService {
    
    @Autowired
    private ApiService apiService;
    
    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryItemByItemId(String itemId) {
      
        String url  = TAOTAO_MANAGE_URL + "/rest/item/"+itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            Item item =MAPPER.readValue(jsonData, Item.class);
            return item;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
