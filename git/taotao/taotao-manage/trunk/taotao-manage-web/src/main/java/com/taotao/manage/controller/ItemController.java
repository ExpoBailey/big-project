package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;
//http://manage.taotao.com/rest/item
@RequestMapping("item")
@Controller
public class ItemController {
    
    @Autowired
    private ItemService itemService;

    /**
     * 添加商品
     * @param item
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> saveItem(Item item,@RequestParam("desc") String desc,@RequestParam("itemParams") String itemParams){
        try {
            this.itemService.saveItem(item,desc,itemParams);
            //201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    

    /**
     * 商品列表管理功能
     */
    // http://manage.taotao.com/rest/item?page=1&rows=30
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EasyUIResult> queryByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        try {
            PageInfo<Item> pageInfo = this.itemService.queryByPage(page, rows);
            EasyUIResult result = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 商品编辑功能实现
     */
    @RequestMapping(method=RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updateItem(Item item,@RequestParam("desc") String desc){
        try {
            this.itemService.updateItem(item,desc);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /**
     * 根据商品id查询商品信息
     */
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Item> queryItemByItemId(@PathVariable("itemId") Long itemId){
        try {
            Item item = new Item();
            item.setId(itemId);
            Item item2 = this.itemService.queryOne(item);
            if (item2 == null) {
                //404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //200
            return ResponseEntity.ok(item2);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
