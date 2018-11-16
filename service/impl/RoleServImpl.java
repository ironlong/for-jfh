package com.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.exception.BusinessException;
import com.shop.common.exception.ShopExceptionConstant;
import com.shop.dao.entity.Node;
import com.shop.dao.entity.NodeRoleKey;
import com.shop.dao.entity.Permission;
import com.shop.dao.entity.Role;
import com.shop.dao.entity.RolePermission;
import com.shop.dao.itf.NodeMapper;
import com.shop.dao.itf.NodeRoleMapper;
import com.shop.dao.itf.PermissionMapper;
import com.shop.dao.itf.RoleMapper;
import com.shop.dao.itf.RolePermissionMapper;
import com.shop.dao.itf.UserRoleMapper;
import com.shop.service.itf.IRoleServ;
/**
 * 
 * 说     明：权限接口实现
 * 
 * 创建时间：2014-4-21
 */
@Service("RoleServImpl")
public class RoleServImpl implements IRoleServ {
	
	private RoleMapper roleMapper;

	private RolePermissionMapper rpMapper;
	
	private UserRoleMapper userRoleMapper;
	@Autowired
	private NodeRoleMapper nodeRoleMapper;
	@Autowired
	private NodeMapper nodeMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	public RolePermissionMapper getRpMapper() {
		return rpMapper;
	}
	@Autowired
	public void setRpMapper(RolePermissionMapper rpMapper) {
		this.rpMapper = rpMapper;
	}
	public RoleMapper getRoleMapper() {
		return roleMapper;
	}
	@Autowired
	public void setRoleMapper(RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
	}
	public UserRoleMapper getUserRoleMapper() {
		return userRoleMapper;
	}
	@Autowired
	public void setUserRoleMapper(UserRoleMapper userRoleMapper) {
		this.userRoleMapper = userRoleMapper;
	}
	/**
	 * 全部角色数量
	 */
	@Override
	public int getAllRoleCount() {
		return roleMapper.selectAllRoleCount();
	}
	/**
	 * 角色列表
	 */
	@Override
	public List<Role> getRoleList(Role role) {
		role.computeStart();
		return roleMapper.selectRoleList(role.getStart(), role.getRows());
	}
	/**
	 * 获得角色
	 */
	@Override
	public Role getRole(String roleId) {
		// TODO Auto-generated method stub
		return roleMapper.selectByPrimaryKey(roleId);
	}
	/**
	 * 获得权限
	 */
	@Override
	public List<Permission> getPermission(String rid) {
		return roleMapper.selectRolePermission(rid);
	}
	/**
	 * 获得角色编码
	 */
	@Override
	public Role getRoleByCode(String roleCode) {
		// TODO Auto-generated method stub
		return roleMapper.getRoleByCode(roleCode);
	}
	/**
	 * 新建角色
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertRole(Role role,List<String> permissions,String[] pids ) {
		// TODO Auto-generated method stub
		int affectRow = roleMapper.insert(role);
		if(affectRow > 0 && !permissions.isEmpty()){//插入角色权限关联表
			for(String permissionId : permissions){
				RolePermission rp = new RolePermission();
				rp.setRoleId(role.getRoleId());
				rp.setPermissionId(permissionId);
				rpMapper.insert(rp);
			}
			//新建一个角色时，查询所有NODEID插入node关联权限表
			List<Node> list = nodeMapper.selectAllNode();
			
			for(Node node : list){
				NodeRoleKey rp = new NodeRoleKey();
				rp.setRoleId(role.getRoleId());
				rp.setNodeId(node.getNodeId());
				nodeRoleMapper.insert(rp);
			}
			
		}
		return affectRow;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateRole(Role role, List<String> permissions,String[] pids) {
		int affectRow = roleMapper.updateByPrimaryKeySelective(role);
		if(affectRow > 0){
			rpMapper.deleteRolePermission(role.getRoleId());
			for(String permissionId : permissions){
				RolePermission rp = new RolePermission();
				rp.setRoleId(role.getRoleId());
				rp.setPermissionId(permissionId);
				rpMapper.insert(rp);
			}
			
			//根据赋权ID，查询NODEID插入node关联权限表
//			List<Permission> list = selectNodesByPermissionIds(pids);
//			nodeRoleMapper.deleteRoleByRoleId(role.getRoleId());
//			for(Permission node : list){
//				NodeRoleKey rp = new NodeRoleKey();
//				rp.setRoleId(role.getRoleId());
//				rp.setNodeId(node.getNodeId());
//				nodeRoleMapper.insert(rp);
//			}
		}
		return affectRow;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateRoleEnabled(String flag, String[] roleIds) {
		// TODO Auto-generated method stub
		if(roleIds != null && roleIds.length > 0 && (flag.equals("1") || flag.equals("0"))){
//			return roleMapper.updateRoleEnabled(flag, roleIds);
		}
		return 0;
	}
	/**
	 * 获得所有角色
	 */
	@Override
	public List<Role> getEnabledRole() {
		// TODO Auto-generated method stub
		return null;//roleMapper.selectEnabledRole();
	}
	/**
	 * 删除角色
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteRole(String roleId) throws BusinessException{
		  
		Role role = roleMapper.selectByPrimaryKey(roleId);
		if(role == null){
			throw new BusinessException(ShopExceptionConstant.FAILURE,"角色不存在");
		}
//		int count = userRoleMapper.getRoleUserCount(Long.parseLong(roleId));
//		if(count > 0){
//			throw new BusinessException(ShopExceptionConstant.FAILURE,"已经有管理员属于本角色,请先更改管理员角色");
//		}
		return roleMapper.deleteByPrimaryKey(roleId);
	}
	@Override
	public List<Permission> selectNodesByPermissionIds(String[] PermissionIds) {
		
		return permissionMapper.selectNodesByPermissionIds(PermissionIds);
	}
}
