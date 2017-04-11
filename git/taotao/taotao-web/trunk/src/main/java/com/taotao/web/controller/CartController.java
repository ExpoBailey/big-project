package com.taotao.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.CookieCartService;
import com.taotao.web.threadlocal.UserThreadLocal;

@RequestMapping("cart")
@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    
    @Autowired
    private CookieCartService cookieCartService;

    /**
     * 添加商品到购物车
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/add/{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            // 未登录
            this.cookieCartService.addItemToCart(itemId,request,response);
        }else{
            // 已登录
            this.cartService.addItemToCart(itemId, user);
        }
        return "redirect:/cart/show.html";
    }

    /**
     * 显示购物车数据
     */
    @RequestMapping(value = "show", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("cart");
        User user = UserThreadLocal.get();
        if (user == null) {
            // 没登录
            List<Cart> carts = this.cookieCartService.queryCartList(request);
            mv.addObject("cartList", carts);
        }else{
            List<Cart> carts = new ArrayList<Cart>();
            carts = this.cartService.queryCartList(user.getId());
            
            mv.addObject("cartList", carts);
        }
        
        return mv;
    }

    /**
     * 更新购物车商品数量
     */
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> updateNumber(@PathVariable("itemId") Long itemId,
            @PathVariable("num") Long num,HttpServletRequest request,HttpServletResponse response) {
        try {
            User user = UserThreadLocal.get();
            if (user == null) {
                // 没登录
                this.cookieCartService.updateNumber(itemId, num,request,response);
            } else {
                // 已登录
                this.cartService.updateNumber(user, itemId, num);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 移除购物车商品
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteItemFromCart(@PathVariable("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            // 没登录
            this.cookieCartService.deleteItemFromCart(itemId,request,response);
        } else {
            // 已登录
            Boolean bool = this.cartService.deleteItemFromCart(user, itemId);
        }
        return "redirect:/cart/show.html";
    }
    
    
}
