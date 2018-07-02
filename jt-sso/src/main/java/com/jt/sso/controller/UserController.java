package com.jt.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	//用户检测 http://sso.jt.com/user/check/{param}/{type}
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public SysResult check(@PathVariable Integer type,@PathVariable String param){
		Boolean b = userService.check(type, param);
		return SysResult.ok(b);
	}
	
	//用户注册 http://sso.jt.com/user/register
	@RequestMapping("/register")
	@ResponseBody
	public SysResult register(User user){
		String username = userService.register(user);
		return SysResult.ok(username);
	}
	
	//用户登录 http://sso.jt.com/user/login
	@RequestMapping("/login")
	@ResponseBody
	public SysResult login(String username, String password){
		try {
			String ticket = userService.login(username, password);
			return SysResult.ok(ticket);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//按ticket查询当前用户的json串 http://sso.jt.com/user/query/{ticket}
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public SysResult queryByTicket(@PathVariable String ticket){
		try{
			String userJson = userService.queryByTicket(ticket);
			return SysResult.ok(userJson);
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, e.getMessage());
		}
	}

}
