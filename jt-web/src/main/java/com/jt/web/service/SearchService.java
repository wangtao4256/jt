package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;

@Service
public class SearchService {
	@Autowired
	private HttpClientService httClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//调用search系统业务接口，进行solr数据全文检索
	public List<Item> getItemListBySearch(String keyWords, Integer page,Integer rows) throws Exception{
		//访问search系统
		String url = "http://search.jt.com/search";
		Map<String,String> params = new HashMap<String,String>();
		params.put("keyWords", keyWords);
		params.put("page", page+"");
		params.put("rows", rows+"");
		
		String jsonData = httClientService.doPost(url, params, "utf-8");
		JsonNode jsonNode = MAPPER.readTree(jsonData);
		JsonNode itemListJsonNode = jsonNode.get("data");
		
		return MAPPER.readValue(itemListJsonNode.traverse(),
                MAPPER.getTypeFactory().constructCollectionType(List.class, Item.class));
		
	}
}
