package com.taotao.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.pojo.Item;

@Service
public class SearchService {
    
    @Autowired
    private ApiService apiService;
    
    @Value("${SEARCH_TAOTAO_URL}")
    private String SEARCH_TAOTAO_URL;

    public EasyUIResult search(String query,Integer page) {
        String url =SEARCH_TAOTAO_URL + "/item/search?keyWord="+query+"&page="+page;
        try {
            String jsonData = this.apiService.doGet(url);
            return EasyUIResult.formatToList(jsonData, Item.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
