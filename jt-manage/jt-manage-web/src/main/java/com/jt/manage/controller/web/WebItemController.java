package com.jt.manage.controller.web;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
public class WebItemController {
	@Autowired
	private ItemService itemService;
	@Autowired
	private RedisService redisService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//为前台系统的按商品的id来查询某个商品  http://manage.jt.com/web/query/item/{id}
	@RequestMapping("/web/query/item/{id}")
	@ResponseBody	//httpClient大多都是json
	public Item getItemById(@PathVariable Long id) throws JsonParseException, JsonMappingException, IOException{
		//Redis缓存读
		String key = "ITEM_"+id;	//唯一 uuid
		String jsonData = redisService.get(key);	//一定能读出来吗?
		if(StringUtils.isNotBlank(jsonData)){
			//有数据
			Item item = MAPPER.readValue(jsonData, Item.class);
			return item;
		}
		
		//如果缓存没有数据，继续执行业务，到数据库去查询数据
		Item item = itemService.queryById(id);
		
		//Redis缓存写
		//redisService.set(key, MAPPER.writeValueAsString(item));	//redis中存放json串
		redisService.set(key, MAPPER.writeValueAsString(item), 60*60*24*10);	//10天后redis会自动删除
		
		return item;
	}
	
	//为前台系统的按商品的id来查询某个商品的详情 http://manage.jt.com/web/query/itemdesc/{id}
	@RequestMapping("/web/query/itemdesc/{id}")
	@ResponseBody	//httpClient大多都是json
	public ItemDesc getItemDescById(@PathVariable Long id){
		return itemService.queryDescById(id);
	}
}
