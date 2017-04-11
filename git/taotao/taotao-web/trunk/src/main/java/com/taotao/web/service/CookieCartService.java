package com.taotao.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.utils.CookieUtils;
import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.User;

@Service
public class CookieCartService {
    private static final String CART_TAOTAO_COOKIE ="CART_TAOTAO_COOKIE";
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private ItemService itemService;

    /**
     * 添加商品到购物车
     * @param itemId
     */
    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        //查询所有购物车数据
        
        List<Cart> carts = queryCartList(request);
        Cart cart = null;
        if (carts !=null) {
            for (Cart c : carts) {
                //判断是否存在商品，如果存在取出该商品
                if (c.getItemId().intValue()==itemId.intValue()) {
                    cart = c;
                    break;
                }
            }
        }else{
            carts = new ArrayList<Cart>();
        }
        
        
        if (cart == null) {
            //不存在该商品
            Item item = this.itemService.queryItemByItemId(itemId);
          
            //添加一条商品
            cart = new Cart();
            
            cart.setItemId(itemId);
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            String[] images = item.getImages();
            if (images==null) {
                cart.setItemImage("");
            }else{
                cart.setItemImage(images[0]);
            }
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setNum(1);
            //把新添的商品放入carts中
            carts.add(cart);
        }else{
            //商品数量相加
            cart.setNum(cart.getNum()+1);
            cart.setUpdated(new Date());
        }
        
        //写入Cookie数据
        try {
            CookieUtils.setCookie(request, response, CART_TAOTAO_COOKIE, MAPPER.writeValueAsString(carts),true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

    /**
     * 查询所有购物车数据
     */
    public List<Cart> queryCartList(HttpServletRequest request) {
        try {
            String jsonData = CookieUtils.getCookieValue(request, CART_TAOTAO_COOKIE,true);
            return MAPPER.readValue(jsonData,MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除商品数据
     */
    public void deleteItemFromCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = queryCartList(request);
        for (Cart cart : carts) {
            if (cart.getItemId().intValue() == itemId.intValue()) {
                carts.remove(cart);
                break;
            }
        }
        //写入cookie
        try {
            CookieUtils.setCookie(request, response, CART_TAOTAO_COOKIE, MAPPER.writeValueAsString(carts),true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    public void updateNumber(Long itemId, Long num, HttpServletRequest request,HttpServletResponse response) {
        
        List<Cart> carts = queryCartList(request);
        for (Cart cart : carts) {
            if (cart.getItemId().intValue() == itemId.intValue()) {
                cart.setNum(num.intValue());
                cart.setUpdated(new Date());
                break;
            }
        }
        //写入cookie
        try {
            CookieUtils.setCookie(request, response, CART_TAOTAO_COOKIE, MAPPER.writeValueAsString(carts),true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
