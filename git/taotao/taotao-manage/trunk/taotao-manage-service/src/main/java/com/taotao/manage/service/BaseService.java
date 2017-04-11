package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {
    
    @Autowired
    private Mapper<T> mapper;
    

    //查询所有
    public List<T> queryAll(){
       return this.mapper.select(null);
    }
    //根据id查询
    public T queryById(Long id){
        return this.mapper.selectByPrimaryKey(id);
    }
    
    //根据条件查询
    public List<T> queryListByWhere(T param){
        List<T> list = this.mapper.select(param);
        return list;
    }
    
    //查询数量
    public Integer queryCount(T param){
        return this.mapper.selectCount(param);
    }
    
    //分页查询
    public PageInfo<T> queryByPageWhere(T param,Integer page ,Integer rows){
        PageHelper.startPage(page, rows);
        List<T> list = this.queryListByWhere(param);
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        return pageInfo;
    }
    
    //查询一条数据
    public T queryOne(T param){
        return this.mapper.selectOne(param);
    }
    //新添记录
    public Integer save(T t){
        if (t.getCreated() == null) {
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        } else if (t.getUpdated() == null) {
            t.setUpdated(t.getCreated());
        }
        int i = this.mapper.insert(t);
        return i;
    }
    //插入选中的记录
    public Integer saveSelective(T t){
        if (t.getCreated() == null) {
            t.setCreated(new Date());
            t.setUpdated(t.getCreated());
        } else if (t.getUpdated() == null) {
            t.setUpdated(t.getCreated());
        }
        return this.mapper.insertSelective(t);
    }
    
    //根据主键更新记录
    public Integer update(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(t);
    }
    
    //根据主键更新
    public Integer updateSelective(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(t);
    }
    
    //删除记录
    public Integer deleteById(Long id){
       return  this.mapper.deleteByPrimaryKey(id);
    }
    
    //批量删除
    public Integer  deleteByIds(List<Object> list,Class<?> clazz){
        Example example = new Example(clazz);
        example.createCriteria().andIn("id", list);
       return this.mapper.deleteByExample(example);
    }
}
