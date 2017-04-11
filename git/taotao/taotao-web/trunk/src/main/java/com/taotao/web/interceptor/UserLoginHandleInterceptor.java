package com.taotao.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.web.controller.UserController;
import com.taotao.web.pojo.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

/**
 * 用户登录拦截器
 *
 */
public class UserLoginHandleInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ticket = CookieUtils.getCookieValue(request, UserController.TAOTAO_TICKE);
        if (StringUtils.isEmpty(ticket)) {
            // 没有登录，跳转登录页
            response.sendRedirect("/user/login.html");
            UserThreadLocal.set(null);
            return false;
        }
        // 检查ticket是否过期

        User user = this.userService.queryUserByTicket(ticket);

        if (user == null) {
            // 没有登录，跳转登录页
            response.sendRedirect("/user/login.html");
            UserThreadLocal.set(null);
            return false;
        }
        //登录成功
        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // TODO Auto-generated method stub

    }

}
