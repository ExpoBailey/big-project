package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;
//http://manage.taotao.com/rest/item/desc/1474391927
@RequestMapping("item/desc")
@Controller
public class ItemDescController {
    
    @Autowired
    private ItemDescService itemDescService;

    /**
     * 根据商品id查询描述信息
     * @param itemId
     * @return
     */
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ItemDesc> queryItemDescByItemId(@PathVariable("itemId") Long itemId){
        try {
            ItemDesc itemDesc =  this.itemDescService.queryItemDescByItemId(itemId);
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
