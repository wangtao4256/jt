package com.jt.manage.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * 为了通用Mapper加上JPA，描述java持久类和数据库表映射关系
 * JPA映射：4个注解就够了
 * 1）类和表的映射
 * 2）属性和数据库表的字段映射
 * 3）标识表的主键
 * 4）标识主键自增
 * 
 * select parent_id,name from tb_item_cat
 */

@Table(name="tb_item_cat")	//映射数据库表，表名tb_item_cat
public class ItemCat extends BasePojo{
	@Id	//主键
	@GeneratedValue(strategy=GenerationType.IDENTITY)	//自增
	private Long id;
	
	@Column(name="parent_id")	//parentId就是类属性，@Column就是表的字段
	private Long parentId;
	
	private String name;
	private Integer status;
	
	@Column(name="sort_order")
	private Integer sortOrder;
	
	@Column(name="is_parent")
	private Boolean isParent;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	
	//给EasyUI增加一个方法，获取树节点名称，不用增加属性，因为json解析时利用get方法
	public String getText(){
		return this.getName();
	}
	//给EasyUI增加的一个方法，标识树干节点状态
	public String getState(){
		return this.getIsParent()?"closed":"open";
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
}
