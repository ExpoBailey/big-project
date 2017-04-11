package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

//http://manage.taotao.com/rest/content?categoryId=48&page=1&rows=20
@RequestMapping("content")
@Controller
public class ContentController {
    
    @Autowired
    private ContentService contentService;

    /**
     * 分页查询广告位内容
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EasyUIResult> queryContentByPageWhere(@RequestParam("categoryId") Long categoryId,
            @RequestParam(value="page",defaultValue="1") Integer page, @RequestParam(value="rows",defaultValue="20") Integer rows) {

            try {
                PageInfo<Content> pageInfo= this.contentService.queryContentByPageWhere(categoryId,page,rows);
                EasyUIResult result = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
                return ResponseEntity.ok(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * http://manage.taotao.com/rest/content
     * 新增广告信息
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> saveContent(Content content){
        try {
            this.contentService.saveContent(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
