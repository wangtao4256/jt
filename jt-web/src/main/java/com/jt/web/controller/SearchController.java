package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jt.web.pojo.Item;
import com.jt.web.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;

	//http://www.jt.com/search.html?q=
	@RequestMapping("/search")
	public String search(String q, @RequestParam(defaultValue="1") Integer page,@RequestParam(defaultValue="20") Integer rows, Model model) throws Exception{
		//访问检索系统
		//防止中文转页面时乱码
		q = new String(q.getBytes("ISO-8859-1"), "UTF-8");
		
		List<Item> itemList = searchService.getItemListBySearch(q, page, rows);
		model.addAttribute("itemList", itemList);
		
		model.addAttribute("query", q);
		
		
		return "search";
	}
}
