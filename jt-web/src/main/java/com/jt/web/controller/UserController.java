package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.util.CookieUtils;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	public static final String JT_COOKIE_NAME = "JT_TICKET";	//固定调用，其他也调用这个名称
	
	//转向注册页面
	@RequestMapping("/register")
	public String register(){
		return "register";
	}
	
	//转向登录页面
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	
	//注册 http://www.jt.com/service/user/doRegister
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult doRegister(User user){
		try{
			String username = userService.register(user);
			return SysResult.ok(username);
		}catch(Exception e){
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//登录 http://www.jt.com/service/user/doLogin?r=0.10970396804623306
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(String username, String password, HttpServletRequest request, HttpServletResponse response){
		String ticket = userService.login(username, password);
		//将ticket写入cookie中，cookie名称只能定死，缺点，目前没有更好方式
		CookieUtils.setCookie(request, response, JT_COOKIE_NAME, ticket);
		
		return SysResult.ok();
	}

}
