package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Item;

@Service
public class CartService{
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@PropertyConfig
	private String CARD_URL;
	
	@PropertyConfig
	private String MANAGE_URL;
	
	//转向我的购物车
	public List<Cart> myCart(Long userId) throws Exception{
		//准备数据
		String url = CARD_URL+"/cart/query/"+userId;
		String jsonData = httpClientService.doGet(url);	//购物车业务接口中获取数据
		
		JsonNode jsonNode = MAPPER.readTree(jsonData);
		JsonNode data = jsonNode.get("data");
		List<Cart> cartList = null;
        if (data.isArray() && data.size() > 0) {
        	//把json转换成List<Cart>
        	cartList = MAPPER.readValue(data.traverse(),
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }
		
		return cartList;
	}

	//添加商品到购物车
	public void add(Long userId, Long itemId, Integer num) throws Exception {
		//判断此用户的此商品是否存在
		String url = CARD_URL+"/cart/get/item/"+userId+"/"+itemId;
		String jsonData = httpClientService.doGet(url);		//cart对象json
		if(StringUtils.isEmpty(jsonData)){	//此用户的此商品不存在
			//新增
			String addUrl = CARD_URL+"/cart/save";
			Map<String,String> params = new HashMap<String,String>();
			params.put("userId", userId+"");
			params.put("itemId", itemId+"");
			params.put("num", num+"");
			
			//从后台系统中获取商品的信息
			String itemUrl = MANAGE_URL+"/item/"+itemId;
			String itemJson = httpClientService.doGet(itemUrl);
			Item item = MAPPER.readValue(itemJson, Item.class);
			params.put("itemTitle", item.getTitle());
			try{
				params.put("itemImage", item.getImage().split(",")[0]);
			}catch(Exception e){
			}
			params.put("itemPrice", item.getPrice()+"");
			
			httpClientService.doPost(addUrl, params, "utf-8");
		}else{			//此用户的此商品已经存在，修改数量= 旧的值+页面上的商品数量
			//修改
			Cart cart = MAPPER.readValue(jsonData, Cart.class);
			Integer newNum = cart.getNum() + num;
			String updateUrl = CARD_URL+"/cart/update/num/"+userId+"/"+itemId+"/"+newNum;
			httpClientService.doGet(updateUrl);
		}
	}
	
	//修改数量 http://www.jt.com/service/cart/update/num/562379/3
	public void updateCart(Long userId, Long itemId, Integer num) throws Exception{
		String url = CARD_URL+"/cart/update/num/"+userId+"/"+itemId+"/"+num;
		httpClientService.doGet(url);
	}
	
	//删除商品，http://cart.jt.com/cart/delete/{userId}/{itemId}
	//
	public void deleteCart(Long userId, Long itemId) throws Exception{
		String url = "http://cart.jt.com/cart/delete/"+userId+"/"+itemId;
		httpClientService.doGet(url);
	}
}
