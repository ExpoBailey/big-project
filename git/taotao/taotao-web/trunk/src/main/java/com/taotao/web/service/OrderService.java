package com.taotao.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.Order;

@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${MANAGE_TAOTAO_URL}")
    private String MANAGE_TAOTAO_URL;

    @Value("${ORDER_TAOTAO_URL}")
    private String ORDER_TAOTAO_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 根据itemId查询商品信息
     * 
     * @param itemId
     * @return
     */
    public Item getItemById(String itemId) {
        String url = MANAGE_TAOTAO_URL + "/rest/item/" + itemId;
        try {
            String jsonData = apiService.doGet(url);
            if (jsonData == null) {
                return null;
            }
            return MAPPER.readValue(jsonData, Item.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建订单
     * 
     * @param order
     * @return
     */
    public String submit(Order order) {
        String url = ORDER_TAOTAO_URL + "/order/create";

        try {
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            if (httpResult.getCode() == 200) {
                // 响应成功
                String json = httpResult.getBody();
                JsonNode jsonNode = MAPPER.readTree(json);
                Integer status = jsonNode.get("status").asInt();
                String data;
                if (status==200) {
                    //创建订单成功
                    data = jsonNode.get("data").asText();
                    return data;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据订单号查询订单信息
     * @param orderID
     * @return
     */
    public Order queryOrderByOrderId(String orderID) {
        String url =ORDER_TAOTAO_URL+"/order/query/"+orderID;
        try {
            String jsonData = this.apiService.doGet(url);
            if (jsonData ==  null) {
                return null;
            }
            return  MAPPER.readValue(jsonData, Order.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
