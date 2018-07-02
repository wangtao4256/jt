package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	//统一中转到views下的jsp页面
public class PageController {
	@RequestMapping("/page/{pageName}")	//利用RESTFul形式，最后一个占位符就是页面名称
	public String goHome(@PathVariable String pageName){
		return pageName;	//将逻辑名就作为jsp名称
	}
}
