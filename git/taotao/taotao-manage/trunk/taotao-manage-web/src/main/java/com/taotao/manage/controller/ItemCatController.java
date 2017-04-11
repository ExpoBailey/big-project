package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.pojo.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("item")
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据商品parent_id查询商品类目
     */
    @RequestMapping(value = "cat/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ItemCat>> queryItemCatByParentId(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        try {
            List<ItemCat> list = this.itemCatService.queryItemCatByParentId(parentId);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    //http://manage.taotao.com/rest/item/cat/all
    @RequestMapping(value="cat/all",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ItemCatResult> queryItemCatAll(){
        try {
            ItemCatResult itemCatResult =  this.itemCatService.queryItemCatAll();
            return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
