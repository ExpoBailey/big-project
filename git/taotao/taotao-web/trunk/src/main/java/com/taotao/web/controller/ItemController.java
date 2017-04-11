package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService ;
    /**
     * 显示商品详情
     * @return
     */
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ModelAndView queryItemByItemId(@PathVariable("itemId") Long itemId){
        ModelAndView mv = new ModelAndView("item");
        mv.addObject("item", this.itemService.queryItemByItemId(itemId));
        mv.addObject("itemDesc", this.itemService.queryItemDescByItemId(itemId));
        mv.addObject("itemParam", this.itemService.queryItemParamToHtml(itemId));
        return mv;
    }
}
