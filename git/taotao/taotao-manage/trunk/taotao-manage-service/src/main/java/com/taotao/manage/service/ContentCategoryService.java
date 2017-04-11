package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ContentCategoryMapper;
import com.taotao.manage.pojo.ContentCategory;

@Service
public class ContentCategoryService extends BaseService<ContentCategory>{
    
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    /**
     * 根据parentId查询分类信息
     * @param parentId
     * @return
     */
    public List<ContentCategory> queryContentCategoryByParentId(Long parentId) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        return  this.queryListByWhere(contentCategory);
      
    }

    /**
     * 新添内容分类功能
     */
    public ContentCategory saveContentCategory(Long parentId, String name) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        contentCategory.setId(null);
        contentCategory.setName(name);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(contentCategory.getCreated());
        
        //判断父分类是否存在子节点， 如果存在更新isparent=true
        ContentCategory contentCategory2 = this.contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!contentCategory2.getIsParent()) {
            contentCategory2.setIsParent(true);
            contentCategory2.setUpdated(new Date());
            this.contentCategoryMapper.updateByPrimaryKeySelective(contentCategory2);
        }
        
        this.save(contentCategory);
        return contentCategory;
    }

    public void updateContentCategory(ContentCategory contentCategory) {
        contentCategory.setUpdated(new Date());
        this.contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
    }

    public void deleteByIds(ContentCategory contentCategory) {
        List<Object> deleteIds = new ArrayList<Object>();
        deleteIds.add(contentCategory.getId());
        
        //递归
        findAllSubNode(deleteIds,contentCategory.getId());
        
        //执行删除
        this.deleteByIds(deleteIds, ContentCategory.class);
        
        //需要判断父类目是否还含有子类目，如果没有，设置isparent=false
        ContentCategory contentCategory2 = new ContentCategory();
        contentCategory2.setParentId(contentCategory.getParentId());
        List<ContentCategory> contentCategories = this.contentCategoryMapper.select(contentCategory2);
        
        if (contentCategories.isEmpty()) {
            ContentCategory contentCategory3 = new ContentCategory();
            contentCategory3.setIsParent(false);
            contentCategory3.setUpdated(new Date());
            contentCategory3.setId(contentCategory.getParentId());
            this.contentCategoryMapper.updateByPrimaryKeySelective(contentCategory3);
        }
       
        
    }

    /**
     * 递归所有子节点
     * @param deleteIds
     * @param id
     */
    private void findAllSubNode(List<Object> deleteIds, Long parentId) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        
        List<ContentCategory> contentCategorys = this.contentCategoryMapper.select(contentCategory);
        for (ContentCategory contentCategory2 : contentCategorys) {
            deleteIds.add(contentCategory2.getId());
            //递归条件
            if (contentCategory2.getIsParent()) {
                findAllSubNode(deleteIds,contentCategory2.getId());
            }
            
        }
    }
}
