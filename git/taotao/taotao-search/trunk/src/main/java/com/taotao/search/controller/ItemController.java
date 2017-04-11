package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIResult;
import com.taotao.search.service.ItemService;

@RequestMapping("item/search")
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 分页查询商品信息
     * 
     * @param keyWord
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EasyUIResult> queryByKeyWords(@RequestParam("keyWord") String keyWord,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "36") Integer rows) {

        try {
            //解决乱码问题
            keyWord = new String(keyWord.getBytes("ISO-8859-1"));
            EasyUIResult easyUIResult = this.itemService.queryByKeyWords(keyWord,page,rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        
        
    }
}
