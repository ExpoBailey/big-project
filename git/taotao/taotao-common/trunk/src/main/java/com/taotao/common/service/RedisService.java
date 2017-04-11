package com.taotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
    

    @Autowired(required = false)//使用到时才注册该对象
    private ShardedJedisPool shardedJedisPool;
    
    /**
     * 执行set
     * @return 
     */
    public <T> T excute(Function<ShardedJedis, T> fun){
        ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return fun.callback(shardedJedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
    }
    

    /**
     * 执行set
     */
    public String set(final String key,final String value){
        return this.excute(new Function<ShardedJedis, String>() {
            @Override
            public String callback(ShardedJedis e) {
                return e.set(key, value);
            }
        });
    }
    
    /**
     * 执行get
     */
    public String get(final String key){
        return  this.excute(new Function<ShardedJedis, String>() {
            @Override
            public String callback(ShardedJedis e) {
                return e.get(key);
            }
        });
    }
    
    /**
     * 设置生存时间
     */
    public Long expire(final String key,final Integer seconds){
        return this.excute(new Function<ShardedJedis, Long>(){
            @Override
            public Long callback(ShardedJedis e) {
                Long expire = e.expire(key, seconds);
                return expire;
            }
            
        });
    }
    
    /**
     * 设置key和value并设置生存时间
     */
    public Long setExpire(final String key,final String value,final Integer seconds){
        return this.excute(new Function<ShardedJedis, Long>(){

            @Override
            public Long callback(ShardedJedis e) {
                e.set(key, value);
                return e.expire(key, seconds);
            }
            
        });
    }
    
    /**
     * 删除键
     */
    public Long del(final String key){
        return this.excute(new Function<ShardedJedis, Long>(){
            @Override
            public Long callback(ShardedJedis e) {
                return e.del(key);
            }
        
        });
    }
    
}
