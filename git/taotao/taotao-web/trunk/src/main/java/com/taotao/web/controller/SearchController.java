package com.taotao.web.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.EasyUIResult;
import com.taotao.web.service.SearchService;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(value = "q", required = true) String query,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        ModelAndView mv = new ModelAndView("search");
        try {
            query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
            mv.addObject("query", query);

            EasyUIResult easyUIResult = this.searchService.search(query, page);
            Integer total = easyUIResult.getTotal();
            mv.addObject("page", page);
            // 总记录数+页面大小-1/页面大小 = 总页数
            mv.addObject("totalPages", (total + 36 - 1) / 36);
            mv.addObject("itemList", easyUIResult.getRows());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mv;
    }
}
