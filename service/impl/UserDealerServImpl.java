/**
 * 用友商城
 *
 * 2014 用友软件股份有限公司-版权所有
 */
package com.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.itf.UserDealerMapper;
import com.shop.service.itf.IUserDealerServ;

/**
 * 
 * 说     明：管理员经销商接口
 * 
 * 创建时间：2014-4-23
 */
@Service("UserDealerServImpl")
public class UserDealerServImpl implements IUserDealerServ {
	
	private UserDealerMapper userDealerMapper;
	
	public UserDealerMapper getUserDealerMapper() {
		return userDealerMapper;
	}
	@Autowired
	public void setUserDealerMapper(UserDealerMapper userDealerMapper) {
		this.userDealerMapper = userDealerMapper;
	}

	/* (non-Javadoc)
	 * @see com.shop.service.itf.IUserDealerServ#batchInsertUserDealer(java.lang.String, java.lang.String[])
	 */
	@Override
	public int batchInsertUserDealer(String userId, String[] dealerIds) {
		// TODO Auto-generated method stub
		return userDealerMapper.insertUserDealer(userId, dealerIds);
	}
	@Override
	public int deleteDealer(String userId) {
		// TODO Auto-generated method stub
		return userDealerMapper.deleteUserDealer(userId);
	}
	@Override
	public int batchDeleteDealer(String dealerId) {
		// TODO Auto-generated method stub
		return userDealerMapper.deleteDealer(dealerId);
	}
	@Override
	public List<String> getDealerIdsByUserId(String userId) {
		// TODO Auto-generated method stub
		return userDealerMapper.getDealerIdsByUserId(userId);
	}
	
}
