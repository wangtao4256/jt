package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.User;

@Service
public class UserService {
	@Autowired
	private HttpClientService httpClientService;
	private static final ObjectMapper MAPPER = new ObjectMapper();

	//从前台系统中去访问sso方法
	//直接返回SysResult对象，但是SysResult写的不规矩，重新封装
	public String register(User user){
		String url = "http://sso.jt.com/user/register";
		Map<String,String> params = new HashMap<String,String>();
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		
		try{
			String jsonData = httpClientService.doPost(url, params, "utf-8");
			//从SysResult对象中解析出data属性值
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			String username = jsonNode.get("data").asText();
			return username;
		}catch(Exception e){
			return null;
		}
	}

	//登录
	public String login(String username, String password){
		String url = "http://sso.jt.com/user/login";
		Map<String,String> params = new HashMap<String,String>();
		params.put("u", username);
		params.put("p", password);
		
		try{
			String jsonData = httpClientService.doPost(url, params, "utf-8");
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			String ticket = jsonNode.get("data").asText();
			return ticket;
		}catch(Exception e){
			return null;
		}
	}
}
