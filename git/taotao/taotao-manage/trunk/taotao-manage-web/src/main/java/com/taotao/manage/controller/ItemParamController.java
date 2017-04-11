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
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

@RequestMapping("item/param")
@Controller
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    /**
     * 新增模板
     */
    // http://manage.taotao.com/rest/item/param/3
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId") Long itemCatId,
            @RequestParam("paramData") String paramData) {
        try {
            this.itemParamService.saveItemParam(itemCatId, paramData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // http://manage.taotao.com/rest/item/param/list?page=1&rows=30
    /**
     * 分页查询规格参数模板
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EasyUIResult> queryItemParamByPage(@RequestParam("page") Integer page,
            @RequestParam("rows") Integer rows) {
        try {
            PageInfo<ItemParam> pageInfo = this.itemParamService.queryItemParamByPage(page, rows);
            EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    //http://manage.taotao.com/rest/item/param/6
    /**
     * 根据类目id查询模板数据
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ItemParam> queryItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId){
        try {
            ItemParam itemParam = this.itemParamService.queryItemParamByItemCatId(itemCatId);
            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
