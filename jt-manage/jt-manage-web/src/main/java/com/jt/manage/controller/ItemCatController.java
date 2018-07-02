package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.pojo.ItemCat;
import com.jt.manage.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	
	//在浏览器地址栏输入请求：http://localhost:8081/itemCatAll，返回值json
	@RequestMapping("/itemCatAll")
	@ResponseBody	//返回的java对象被springmvc底层利用jackson提供工具类方法转成json。
	public List<ItemCat> queryAll(){
		int[] x = {1,2,3};
		for(int y:x){
			System.out.println(y);
		}
		
		List<ItemCat> itemList = itemCatService.queryAll();
		return itemList;
	}
	
	//用户通过ajax，传递id参数，返回值json  /item/cat/list
	@RequestMapping("list")	//自动拼接一个/
	@ResponseBody
	public List<ItemCat> queryListById(@RequestParam(defaultValue="0") String id){	//利用springmvc提供设置默认值
		return itemCatService.queryById(id);
	}
}
