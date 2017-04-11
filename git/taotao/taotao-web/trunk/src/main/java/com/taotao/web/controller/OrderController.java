package com.taotao.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.Order;
import com.taotao.web.pojo.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;
//http://www.taotao.com/order/1474391928.html
@RequestMapping("order")
@Controller
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;

    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable("itemId") String itemId){
        ModelAndView mv = new ModelAndView("order");
        Item item = this.orderService.getItemById(itemId);
        mv.addObject("item", item);
        return mv;
    }
    
    //http://www.taotao.com/service/order/submit
    /**
     * 处理提交定单
     * @param order
     * @param ticket
     * @return
     */
    @RequestMapping(value="submit",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submit(Order order,@CookieValue(UserController.TAOTAO_TICKE) String ticket){
        
        User user = UserThreadLocal.get();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        
        Map<String, Object> result = new HashMap<String, Object>();
        String orderNumber = this.orderService.submit(order);
        if (orderNumber == null) {
            //创建失败
            result.put("status", "500");
            return result;
        }else{
            //创建成功
            result.put("status", "200");
            result.put("data", orderNumber);
        }
        return result;
    }
    
    /**
     * 去下单成功页
     */
    @RequestMapping(value="success",method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView toSuccess(@RequestParam("id") String orderID){
        ModelAndView mv = new ModelAndView("success");
        mv.addObject("order", this.orderService.queryOrderByOrderId(orderID));
        mv.addObject("date", new DateTime().plusDays(2).toString("MM月dd日"));
        return mv;
    }
    
    /**
     * 去下单页面
     */
    @RequestMapping(value="create",method=RequestMethod.GET)
    public ModelAndView toCart(){
        ModelAndView  mv  = new ModelAndView("order-cart");
        mv.addObject("carts", this.cartService.queryCartList(UserThreadLocal.get().getId()));
        return mv;
    }
    
}
