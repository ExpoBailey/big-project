package com.taotao.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;
//http://manage.taotao.com/rest/item/param/item/query/1474391930
@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;
    
    /**
     * 根据商品id查询规格参数数据
     * @param itemId
     * @return
     */
    @RequestMapping(value="query/{itemId}",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ItemParamItem> queryItemParamItemByItemId(@PathVariable("itemId") Long itemId){
        try {
            ItemParamItem itemParamItem =  this.itemParamItemService.queryItemParamItemByItemId(itemId);
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
