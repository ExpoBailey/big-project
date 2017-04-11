package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

//http://manage.taotao.com/rest/content/category
@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据parentId查询分类信息
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ContentCategory>> queryContentCategoryByParentId(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        try {
            List<ContentCategory> list = this.contentCategoryService.queryContentCategoryByParentId(parentId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新添内容分类功能
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ContentCategory> saveContentCategory(@RequestParam("parentId") Long parentId,
            @RequestParam("name") String name) {
        try {
            ContentCategory contentCategory =  this.contentCategoryService.saveContentCategory(parentId,name);
            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     *  重名命
     */
    //http://manage.taotao.com/rest/content/category
    @RequestMapping(method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory){
        try {
            this.contentCategoryService.updateContentCategory(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /**
     * 批量删除
     */
    @RequestMapping(method=RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Void> deleteByIds(ContentCategory contentCategory){
        
        try {
            this.contentCategoryService.deleteByIds(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      
    }

   
}
