package com.jt.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;

@Service
public class CartService extends BaseService<Cart>{
	@Autowired
	private CartMapper cartMapper;
	
	//我的购物车
	public List<Cart> queryListByUserId(Long userId){
		Cart param = new Cart();
		param.setUserId(userId);
		
		return cartMapper.select(param);
	}
	
	//某个用户是已经有此商品
	public Cart queryItemByUserId(Long userId, Long itemId){
		Cart param = new Cart();
		param.setUserId(userId);
		param.setItemId(itemId);
		
		return super.queryByWhere(param);
	}
	
	//更新商品数量
	public void updateCart(Cart cart){
		cartMapper.updateCart(cart);
	}
}
