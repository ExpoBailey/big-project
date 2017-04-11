package com.taotao.sso.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.mapper.Mapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {

    private static final Map<Integer, Boolean> TYPES = new HashMap<Integer, Boolean>();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    static {
        TYPES.put(1, true);
        TYPES.put(2, true);
        TYPES.put(3, true);
    }

    @Autowired
    private UserMapper userMapper;

    /**
     * 检查用户是否合法
     * 
     * @param param
     * @param type
     * @return
     * @throws Exception
     */
    public Boolean check(String param, Integer type) throws Exception {

        if (!TYPES.containsKey(type)) {
            throw new Exception("参数不合法！type :1,2,3");
        }
        User user = new User();

        switch (type) {
        case 1:
            user.setUsername(param);
            break;
        case 2:
            user.setPhone(param);
            break;
        case 3:
            user.setEmail(param);
            break;
        default:
            break;
        }
        // True：数据可用，false：数据不可用
        return userMapper.select(user).isEmpty();
    }

    /**
     * 用户注册
     * 
     * @param user
     */
    public void register(User user) {
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setId(null);
        this.userMapper.insertSelective(user);

    }

    /**
     * 用户登录
     * 
     * @param userName
     * @param password
     * @return
     * @throws JsonProcessingException
     */
    public String login(String userName, String password) throws JsonProcessingException {
        User user = new User();
        user.setUsername(userName);
        User user1 = this.userMapper.selectOne(user);

        if (user1 == null) {
            return null;
        }
        if (!StringUtils.equals(user1.getPassword(), DigestUtils.md5Hex(password))) {
            // 登录失败
            return null;
        }
        
        // 登录成功
        String ticket = DigestUtils.md5Hex(System.currentTimeMillis() + userName);
        this.redisService.setExpire(ticket, MAPPER.writeValueAsString(user1), 3600);
        return ticket;
    }

    /**
     * 通过ticket查询用户信息
     * 
     * @param ticket
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public User queryUserByTicket(String ticket) throws JsonParseException, JsonMappingException, IOException {
        String jsonData = this.redisService.get(ticket);
        if (jsonData == null) {
            return null;
        }
        
        //想到session有效时间，每刷新一次重置session时间。
        this.redisService.expire(ticket, 3600);
        return MAPPER.readValue(jsonData, User.class);
    }

}
