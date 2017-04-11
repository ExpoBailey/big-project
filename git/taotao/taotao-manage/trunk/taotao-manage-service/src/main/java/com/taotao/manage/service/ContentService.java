package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content>{
    
    @Autowired
    private ContentMapper contentMapper;

    /**
     * 分页查询广告位内容
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<Content> queryContentByPageWhere(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Content> list = contentMapper.queryContentByPageWhere(categoryId);
        PageInfo<Content> pageInfo = new PageInfo<Content>(list);
        return pageInfo;
    }

    /**
     * 新增广告信息
     * @param content
     */
    public void saveContent(Content content) {
        content.setId(null);
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        this.contentMapper.insertSelective(content);
    }

}
