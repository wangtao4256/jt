package com.jt.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jt.common.service.RedisService;
import com.jt.common.spring.exetend.PropertyConfig;
import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;

@Service
public class ItemService extends BaseService<Item>{
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired	//和spring集成后，spring给我们实例化
	private RabbitTemplate rabbitTemplate;
	
	@PropertyConfig
	private String REPOSITORY_PATH;
	@PropertyConfig
	private String IMAGE_BASE_URL;
	
	@Autowired
	private RedisService redisService;
	
	public EasyUIResult queryItemList(Integer page, Integer rows){
		System.out.println(REPOSITORY_PATH);
		
		//怎么将数据封装到EasyUIResult
		PageHelper.startPage(page, rows);	//标识分页开始，准备拦截，下面的第一条执行查询的SQL
		List<Item> itemList = itemMapper.queryItemList();
		//List<Item> itemList2 = itemMapper.queryItemList();
		PageInfo<Item> pageInfo = new PageInfo<Item>(itemList);
		//EasyUIResult提供多个构造方法，以一个属性写入
		//为线程安全，要把数据封装到PageInfo对象
		return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
	}
	
	//新增
	public void saveItem(Item item, String desc){
		//设置默认值
		item.setStatus(1);			//正常
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());
		
		itemMapper.insertSelective(item);
		
		//新增商品描述
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());	//mybatis会回置值
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(itemDesc.getCreated());
		itemDescMapper.insertSelective(itemDesc);
	}

	//修改
	public void updateItem(Item item, String desc){
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		
		//修改商品信息，把redis缓存的内容清除
		String key = "ITEM_"+item.getId();
		//redisService.del(key);
		
		//换成rabbitmq机制
		String routingKey = "item_update";
		//mq是公用的，内容不要太多，尽量小，网络传输快，消息传递快，消费也快
		rabbitTemplate.convertAndSend(routingKey, key);
		
		//修改商品描述
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
	}
	
	//删除
	public void deleteItem(String[] ids){
		//删除商品描述信息
		itemDescMapper.deleteByIDS(ids);
		itemMapper.deleteByIDS(ids);
	}
	
	//按商品id查询商品描述
	public ItemDesc queryDescById(Long id){
		return itemDescMapper.selectByPrimaryKey(id);
	}
}