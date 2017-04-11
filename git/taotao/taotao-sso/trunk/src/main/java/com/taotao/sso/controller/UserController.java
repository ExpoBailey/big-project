package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 检查用户是否合法
     * 
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Boolean> check(@PathVariable("param") String param,
            @PathVariable("type") Integer type) {
        try {
            Boolean bool = this.userService.check(param, type);
            // 200
            return ResponseEntity.ok(bool);
        } catch (Exception e) {
            // 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 用户注册
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> register(User user) {
        try {
            // ToDo 检查
            this.userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> login(@RequestParam("u") String userName, @RequestParam("p") String password) {
        try {
            String ticket = this.userService.login(userName,password);
            //200
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * http://sso.taotao.com/user/fe5cb546aeb3ce1bf37abcb08a40493e
     * 通过ticket查询用户信息
     */
    @RequestMapping(value="{ticket}",method=RequestMethod.GET)
    public ResponseEntity<User> queryUserByTicket(@PathVariable("ticket") String ticket){
        try {
            User user =  this.userService.queryUserByTicket(ticket);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      
    }
}
