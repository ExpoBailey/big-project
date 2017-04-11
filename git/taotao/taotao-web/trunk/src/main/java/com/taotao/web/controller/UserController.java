package com.taotao.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.pojo.User;
import com.taotao.web.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    public static final String TAOTAO_TICKE ="TT_TICKET";

    /**
     * 显示注册页面
     * 
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register() {
        return "register";
    }

    /**
     * http://www.taotao.com/service/user/doRegister
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(User user) {
        Map<String, Object> result = new HashMap<String, Object>();
        Boolean bool = this.userService.doRegister(user);
        if (bool) {
            // 注册成功
            result.put("status", "200");
        } else {
            result.put("status", "500");
        }

        return result;

    }

    /**
     * 显示登录页
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    // http://www.taotao.com/service/user/doLogin?r=0.21973024703305377
    /**
     * 用户登录
     */
    @RequestMapping(value="doLogin",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(@RequestParam("username") String username,
            @RequestParam("password") String password,HttpServletRequest request,HttpServletResponse response) {
        String ticket = this.userService.doLogin(username,password);
        Map<String, Object> result = new HashMap<String, Object>();
        if (null == ticket) {
            //登录失败
            result.put("status", "500");
            return result;
        }else{
            //登录成功
            result.put("status", "200");
            //写入cookie
            CookieUtils.setCookie(request, response, TAOTAO_TICKE, ticket);
        }
        return result;
        
    }
}
