 
package com.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.Node;
import com.shop.dao.entity.UserRole;
import com.shop.dao.itf.NodeMapper;
import com.shop.dao.itf.UserRoleMapper;
import com.shop.service.itf.IUserRoleServ;

/**
 * 说     明：用户权限相关业务类
 * 
 * 创建时间：2014-4-23
 */
@Service("UserRoleServImpl")
public class UserRoleServImpl implements IUserRoleServ {
	
	private UserRoleMapper userRoleMapper;
	@Autowired
	private NodeMapper nodeMapper;
	
	public UserRoleMapper getUserRoleMapper() {
		return userRoleMapper;
	}
	@Autowired
	public void setUserRoleMapper(UserRoleMapper userRoleMapper) {
		this.userRoleMapper = userRoleMapper;
	}

	/* (non-Javadoc)
	 * @see com.shop.service.itf.IUserRoleServ#insertSelective(com.shop.dao.entity.UserRole)
	 */
	@Override
	public int insertSelective(UserRole record) {
		// TODO Auto-generated method stub
		return userRoleMapper.insertSelective(record);
	}

	/* (non-Javadoc)
	 * @see com.shop.service.itf.IUserRoleServ#insert(com.shop.dao.entity.UserRole)
	 */
	@Override
	public int insert(UserRole record) {
		// TODO Auto-generated method stub
		return userRoleMapper.insert(record);
	}
	@Override
	public int deleteRole(String userId) {
		// TODO Auto-generated method stub
		return userRoleMapper.deleteUserRole(userId);
	}
	@Override
	public List<Node> listNodeByUserId(String userId){
		return (List<Node>) nodeMapper.listNodeByUserId(userId);
	}
	@Override
	public UserRole getRoleUserByUserId(String userId) {
		// TODO Auto-generated method stub
		return userRoleMapper.getRoleUserByUserId(userId);
	}
}
