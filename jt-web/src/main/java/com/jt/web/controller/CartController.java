package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.web.pojo.Cart;
import com.jt.web.service.CartService;
import com.jt.web.threadlocal.UserThreadLocal;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	//转向我的购物车
	//http://www.jt.com/cart/show.html
	@RequestMapping("/show")
	public String show(Model model) throws Exception{
		//准备数据
		Long userId = UserThreadLocal.getUserId();
		List<Cart> cartList = cartService.myCart(userId);
		model.addAttribute("cartList", cartList);
		
		return "cart";
	}
	
	//添加商品到购物车 http://www.jt.com/cart/add/562379.html
	@RequestMapping("/add/{itemId}")	//springmvc既可以从RESTFul URL接收参数，也可以同时从post请求接参
	public String add(@PathVariable Long itemId, Integer num) throws Exception{
		Long userId = UserThreadLocal.getUserId();
		cartService.add(userId, itemId, num);
		
		return "redirect:/cart/show.html";	//重定向我的购物车
	}
	
	//修改数量，http://www.jt.com/service/cart/update/num/562379/3
	@RequestMapping("/update/num/{itemId}/{num}")
	public String update(@PathVariable Long itemId, @PathVariable Integer num) throws Exception{
		Long userId = UserThreadLocal.getUserId();
		cartService.updateCart(userId, itemId, num);
		
		return "redirect:/cart/show.html";
	}
	
	//删除商品，http://www.jt.com/cart/delete/562379.html
	@RequestMapping("/delete/{itemId}")
	public String delete(@PathVariable Long itemId) throws Exception{
		Long userId = UserThreadLocal.getUserId();
		cartService.deleteCart(userId, itemId);
		
		return "redirect:/cart/show.html";
	}
}
