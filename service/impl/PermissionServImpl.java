/**
 * 用友商城
 *
 * 2014 用友软件股份有限公司-版权所有
 */
package com.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.Permission;
import com.shop.dao.itf.PermissionMapper;
import com.shop.service.itf.IPermissionServ;

/**
 * 作     者：宋怀龙
 * 
 * 说     明：权限管理
 * 
 * 创建时间：2014-4-19
 */
@Service("PermissionServImpl")
public class PermissionServImpl implements IPermissionServ {
	
	private PermissionMapper permissionMapper;
	
	public PermissionMapper getPermissionMapper() {
		return permissionMapper;
	}

	@Autowired
	public void setPermissionMapper(PermissionMapper permissionMapper) {
		this.permissionMapper = permissionMapper;
	}

	/* (non-Javadoc)
	 * @see com.shop.service.itf.IPermissionServ#getAllPermission()
	 */
	@Override
	public List<Permission> getAllPermission() {
		return permissionMapper.selectAllPermission();
	}

	@Override
	public int getAllPermissionCount() {
		// TODO Auto-generated method stub
		return permissionMapper.selectAllPermissionCount();
	}

	@Override
	public Permission getPermission(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Permission getPermissionByCode(String permissionCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertPermission(Permission permissions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updatePermission(Permission permissions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectAllPermissionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
