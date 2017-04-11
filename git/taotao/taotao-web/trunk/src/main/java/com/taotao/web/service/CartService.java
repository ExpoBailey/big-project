package com.taotao.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.User;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    @Value("${CART_TAOTAO_URL}")
    private String CART_TAOTAO_URL;

    @Value("${MANAGE_TAOTAO_URL}")
    private String MANAGE_TAOTAO_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 添加商品到购物车
     * 
     * @param itemId
     * @param user
     * @return
     */
    public Boolean addItemToCart(Long itemId, User user) {

        String url = CART_TAOTAO_URL + "/rest/cart";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", user.getId());
        param.put("itemId", itemId);

        String itemUrl = MANAGE_TAOTAO_URL + "/rest/item/" + itemId;
        String jsonData;
        try {
            jsonData = this.apiService.doGet(itemUrl);
            Item item = MAPPER.readValue(jsonData, Item.class);

            param.put("itemTitle", item.getTitle());
            if (item.getImage() == null) {
                param.put("itemImage", "");
            }
            param.put("itemImage", item.getImages()[0]);

            param.put("itemPrice", item.getPrice());

            param.put("num", 1);// 后面实践做的内容

            HttpResult httpResult = this.apiService.doPost(url, param);
            // 判断购物车商品添加或更新
            if (httpResult.getCode() == 201 || httpResult.getCode() == 204) {
                return true;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 显示购物车数据
     * 
     * @param id
     * @return
     */
    public List<Cart> queryCartList(Long userId) {
        String url = CART_TAOTAO_URL + "/rest/cart/" + userId;
        try {
            String jsonData = this.apiService.doGet(url);
            return MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新购物车商品数量
     */
    public Boolean updateNumber(User user, Long itemId, Long num) {
        
        String url =CART_TAOTAO_URL + "/rest/cart/"+user.getId()+"/"+itemId+"/"+num;
        
        try {
            HttpResult httpResult = this.apiService.doPut(url);
            if (httpResult.getCode()==204) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 移除购物车商品
     */
    public Boolean deleteItemFromCart(User user, Long itemId) {
        String url =CART_TAOTAO_URL+ "/rest/cart/"+user.getId()+"/"+itemId;
        try {
            HttpResult httpResult = this.apiService.doDelete(url);
            if (httpResult.getCode()==204) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
