package com.jt.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.common.util.CookieUtils;
import com.jt.web.threadlocal.UserThreadLocal;

@Controller
public class IndexController {
	//http://www.jt.com/index.html
	@RequestMapping("/index")
	public String goHome(){
		return "index";
	}
	
	//退出 http://www.jt.com/user/logout.html
	@RequestMapping("/user/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		//清除cookie值，用户就无法老在登录状态
		CookieUtils.deleteCookie(request, response, UserController.JT_COOKIE_NAME);
		UserThreadLocal.set(null);
		
		return "index";
	}	
}
