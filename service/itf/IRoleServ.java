package com.shop.service.itf;

import java.util.List;

import com.shop.common.exception.BusinessException;
import com.shop.dao.entity.Node;
import com.shop.dao.entity.Permission;
import com.shop.dao.entity.Role;

public interface IRoleServ {
	int getAllRoleCount();
	List<Role> getRoleList(Role role);
	Role getRole(String roleId);
	List<Permission> getPermission(String rid);
	Role getRoleByCode(String roleCode);
	int insertRole(Role role,List<String> permissions,String[] pids );
	int updateRole(Role role,List<String> permissions,String[] pids);
	int updateRoleEnabled(String flag,String[] roleIds);
	List<Role> getEnabledRole();
	int deleteRole(String roleId) throws BusinessException;	
	List<Permission> selectNodesByPermissionIds(String[] PermissionIds);
}
