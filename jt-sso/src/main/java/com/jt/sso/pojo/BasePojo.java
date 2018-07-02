package com.jt.sso.pojo;

import java.util.Date;

//抽象类，封装两个日期，其他类pojo都来继承
public abstract class BasePojo {
	private Date created;
	private Date updated;
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
}
