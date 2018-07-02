package com.jt.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.pojo.Item;
import com.jt.web.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	//item/10500751966.html
	@RequestMapping("/item/{itemId}")
	public String getItem(@PathVariable Long itemId, Model model) throws Exception{
		Item item = itemService.getItemById(itemId);
		model.addAttribute("item", item);
		
		return "item";	//商品详情页面
	}
}
