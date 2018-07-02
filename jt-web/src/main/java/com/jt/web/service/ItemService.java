package com.jt.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.service.RedisService;
import com.jt.web.pojo.Item;

@Service
public class ItemService {
	@Autowired
	private RedisService redisService;
	@Autowired
	private HttpClientService httpClientService;
	//jackson工具类，java对象转json，json串转换java对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//从后台系统中获取商品详情
	public Item getItemById(Long itemId) throws Exception {
		//1.判断缓存是否有数据，有直接返回，没继续执行，不能抛异常
		String itemKey = "ITEM_" + itemId;	//唯一性
		String jsonItem = redisService.get(itemKey);	//第一次就读不到
		if(StringUtils.isNotEmpty(jsonItem)){
			Item item = MAPPER.readValue(jsonItem, Item.class);
			return item;
		}
		
		String url = "http://manage.jt.com/item/"+itemId;
		String jsonData = httpClientService.doGet(url);	//item对象
		
		//把json串转成java对象 Item.java
		Item item = MAPPER.readValue(jsonData, Item.class);
		
		//2.业务执行完要把数据缓存，如果使用LRU
		redisService.set(itemKey, jsonData, 60*60*24*7);	//7day
		
		return item;
	}

}
