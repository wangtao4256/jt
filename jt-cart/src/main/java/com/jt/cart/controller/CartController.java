package com.jt.cart.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.cart.pojo.Cart;
import com.jt.cart.service.CartService;
import com.jt.common.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	//我的购物车 http://cart.jt.com/cart/query/{userId}
	@RequestMapping("/query/{userId}")
	@ResponseBody
	public SysResult queryListByUserId(@PathVariable Long userId){
		return SysResult.ok(cartService.queryListByUserId(userId));
	}
	
	//商品新增，保存商品 http://cart.jt.com/cart/save
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveCart(Cart cart){
		cart.setCreated(new Date());
		cart.setUpdated(cart.getCreated());
		
		cartService.saveSelective(cart);
		return SysResult.ok();
	}
	
	//商品数量修改，http://cart.jt.com/cart/update/num/{userId}/{itemId}/{num}
	@RequestMapping("/update/num/{userId}/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(@PathVariable Long userId,@PathVariable Long itemId,@PathVariable Integer num){
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		cart.setNum(num);
		
		cartService.updateCart(cart);
		return SysResult.ok();
	}
	
	//商品删除，http://cart.jt.com/cart/delete/{userId}/{itemId}
	@RequestMapping("/delete/{userId}/{itemId}")
	@ResponseBody
	public SysResult delete(@PathVariable Long userId,@PathVariable Long itemId){
		Cart param = new Cart();
		param.setUserId(userId);
		param.setItemId(itemId);
		
		cartService.deleteByWhere(param);
		return SysResult.ok();
	}
	
	//某个用户的某个商品是否存在 ，http://cart.jt.com/cart/item/{userId}/{itemId}
	@RequestMapping("/get/item/{userId}/{itemId}")
	@ResponseBody
	public Cart queryItemByUserId(@PathVariable Long userId, @PathVariable Long itemId){
		return cartService.queryItemByUserId(userId, itemId);
	}
}

