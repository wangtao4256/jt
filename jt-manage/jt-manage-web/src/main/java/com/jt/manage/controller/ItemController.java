package com.jt.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.EasyUIResult;
import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	//商品列表页面，/item/query	EasyUI.datagrid组件要求：page当前页，rows每页条数
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult queryItemList(Integer page, Integer rows){
		return itemService.queryItemList(page, rows);
	}
	
	//商品新增	/item/save
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item, String desc){
		try{
			itemService.saveItem(item, desc);
			return SysResult.ok();
		}catch(Exception e){
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//商品修改  /item/update
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item, String desc){
		try{
			itemService.updateItem(item, desc);
			return SysResult.ok();
		}catch(Exception e){
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//商品删除 /item/delete
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItem(String[] ids){
		try{
			itemService.deleteItem(ids);
			return SysResult.ok();
		}catch(Exception e){
			return SysResult.build(201, e.getMessage());
		}
	}
	
	//商品描述查询 http://manage.jt.com/item/query/item/desc/1474391951
	@RequestMapping("query/item/desc/{id}")
	@ResponseBody
	public SysResult queryDescById(@PathVariable Long id){
		ItemDesc itemDesc = itemService.queryDescById(id);
		return SysResult.ok(itemDesc);
	}
}
