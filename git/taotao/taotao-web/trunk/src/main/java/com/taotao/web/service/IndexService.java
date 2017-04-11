package com.taotao.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.web.pojo.Content;

@Service
public class IndexService {

    @Autowired
    private ApiService apiService;
    
    @Autowired
    private RedisService redisService;

    @Value("${MANAGE_TAOTAO_URL}")
    private String MANAGE_TAOTAO_URL;

    @Value("${MANAGE_TAOTAO_INDEXAD1}")
    private String MANAGE_TAOTAO_INDEXAD1;
    
    @Value("${MANAGE_TAOTAO_INDEXAD2}")
    private String MANAGE_TAOTAO_INDEXAD2;
    
    private static final String REDIS_AD1_KEY="TAOTAO_WEB_INDEX_AD1";
    
    private static final Integer REDIS_AD1_TIME = 60*60*24;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String getIndexAd1() {
        String url = MANAGE_TAOTAO_URL + MANAGE_TAOTAO_INDEXAD1;
        
        //先命中
        try {
            String jsondata = this.redisService.get(REDIS_AD1_KEY);

            if (StringUtils.isNoneBlank(jsondata)) {
                //命中
                return jsondata;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        try {
            String jsonData = this.apiService.doGet(url);
            // 反序列化对象
            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
            // 取出content集合
            List<Content> contents = (List<Content>) easyUIResult.getRows();

            // 封装前台格式数据
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

            for (Content content : contents) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("srcB", content.getPic());
                map.put("height", 240);
                map.put("alt", content.getTitle());
                map.put("width", 670);
                map.put("src", content.getPic());
                map.put("widthB", 550);
                map.put("href", content.getUrl());
                map.put("heightB", 240);
                result.add(map);
            }

            String valueAsString = MAPPER.writeValueAsString(result);
            try {
                //加入缓存
                this.redisService.setExpire(REDIS_AD1_KEY, valueAsString, REDIS_AD1_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return MAPPER.writeValueAsString(result);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public String getIndexAd2() {
        String url = MANAGE_TAOTAO_URL + MANAGE_TAOTAO_INDEXAD2;
        // 封装前台格式数据
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            String jsonData = this.apiService.doGet(url);
            // 反序列化对象
            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
            // 取出content集合
            if (easyUIResult!=null) {
                List<Content> contents = (List<Content>) easyUIResult.getRows();
                // 封装前台格式数据
                for (Content content : contents) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("width", 310);
                    map.put("height", 70);
                    map.put("src", content.getPic());
                    map.put("href", content.getUrl());
                    map.put("alt", content.getTitle());
                    map.put("widthB", 210);
                    map.put("heightB", 70);
                    map.put("srcB", content.getPic2());
                    result.add(map);
                }

            }
           
            return MAPPER.writeValueAsString(result);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
