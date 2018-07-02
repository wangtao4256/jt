package com.jt.sso.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jt.sso.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.sso.mapper.UserMapper;

@Service
public class UserService extends BaseService<User>{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisService redisService;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	//用户名、手机、邮箱校验
	public Boolean check(Integer typeValue, String value){
		String name = "";
		if(typeValue==1){
			name = "username";
		}else if(typeValue==2){
			name = "phone";
		}else{
			name = "email";
		}
		
		//拼接参数，形成一个map
		Map<String,String> param = new HashMap<String,String>();
		param.put("typename", name);
		param.put("param", value);
			
		int countNum = userMapper.check(param);
		if(countNum>0){
			return true;
		}else{
			return false;
		}
	}
	
	//注册	
	public String register(User user){
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));	//MD5加密
		user.setEmail(user.getPhone());	//投机取巧
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		
		userMapper.insertSelective(user);
		
		return user.getUsername();
	}
	
	//登录
	public String login(String username,String password) throws JsonProcessingException{
		User param = new User();
		param.setUsername(username);	//where username=?
		
		//习惯，按用户名查询
		User curUser = super.queryByWhere(param);
		String newPassword = DigestUtils.md5Hex(password);
		//页面的密码加密后和数据库的密码比较
		if(newPassword.equals(curUser.getPassword())){
			//形成ticket，写入redis，把ticket传递前台cookie
			//第一，唯一；第二，混淆让一般人看到后无法识别这是一个什么内容，yyyydd
			String ticket = DigestUtils.md5Hex("JT_TICKET_"+System.currentTimeMillis()+username+curUser.getId());	//唯一
			
			//把user对象放入redis，password的密码如果直接转换密码就泄露
			//放入redis中后，可以不需要httpsession，把有状态链接变成“无状态”
			redisService.set(ticket, MAPPER.writeValueAsString(curUser));
			
			return ticket;
		}else{
			return null;
		}
	}
	
	//按ticket查询用户信息
	public String queryByTicket(String ticket){
		return redisService.get(ticket);
	}
}
