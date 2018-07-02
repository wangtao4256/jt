package com.jt.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat>{
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	//没有的方法自行扩展
	public List<ItemCat> queryById(String id){
		return itemCatMapper.queryById(id);
	}
}