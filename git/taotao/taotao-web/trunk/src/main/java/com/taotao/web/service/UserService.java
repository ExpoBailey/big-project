package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.pojo.User;

@Service
public class UserService {

    @Autowired
    private ApiService apiService;
    
    @Value("${SSO_TAOTAO_URL}")
    private String SSO_TAOTAO_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * 用户注册
     * @param user
     * @return
     */
    public Boolean doRegister(User user) {
        String url = SSO_TAOTAO_URL+"/user/register";
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        if (user.getPhone()!=null) {
            map.put("phone", user.getPhone());
        }
        if (user.getEmail()!=null) {
            map.put("email", user.getEmail());
        }
        
        try {
            HttpResult httpResult = this.apiService.doPost(url, map);
            if (httpResult.getCode() == 201) {
                //成功
                return true;
            }else{
                return false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public String doLogin(String username, String password) {
        String url =SSO_TAOTAO_URL + "/user/login";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("u", username);
        param.put("p", password);
        
        try {
            HttpResult httpResult = this.apiService.doPost(url, param);
            if (httpResult.getCode()==200) {
                 String body = httpResult.getBody();
                 return body;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据ticket查询用户
     * @param ticket
     * @return
     */
    public User queryUserByTicket(String ticket) {
        String url =SSO_TAOTAO_URL + "/user/"+ticket;
        try {
            String jsonData = this.apiService.doGet(url);
            if (jsonData == null) {
                return null;
            }
            User user = MAPPER.readValue(jsonData, User.class);
            return user;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
