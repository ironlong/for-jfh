package com.shop.service.itf.shiro;

import java.util.Collection;

import org.apache.shiro.session.Session;

import com.shop.dao.entity.User;

public interface IShiroAuth {
	User findSubject(String principals);
	
	
	/**
	  * 方法描述：消除其session,让用户强制退出系统
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-16 上午11:41:18
	  */
	int letUserOut(String principals);
	
	/**
	  * 方法描述：获取当前系统所有登录用户session
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-16 上午11:41:53
	  */
	Collection<Session> getAllSessions();
}
