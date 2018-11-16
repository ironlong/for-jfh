package com.shop.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.exception.BusinessException;
import com.shop.common.exception.ShopExceptionConstant;
import com.shop.common.utils.DataTimeUtil;
import com.shop.dao.entity.Role;
import com.shop.dao.entity.User;
import com.shop.dao.entity.UserRole;
import com.shop.dao.itf.UserMapper;
import com.shop.service.itf.IRoleServ;
import com.shop.service.itf.IUserDealerServ;
import com.shop.service.itf.IUserRoleServ;
import com.shop.service.itf.IUserServ;
/**
 * 
 * 说     明：管理员服务层
 * 
 * 创建时间：2014-4-22
 */
@Service("UserServImpl")
public class UserServImpl implements IUserServ {
	
	private UserMapper userMapper;
	
	private IRoleServ roleServ;
	
	
	private IUserRoleServ userRoleServ;
	
	private IUserDealerServ userDealerServ;
	
	 
	
	public IRoleServ getRoleServ() {
		return roleServ;
	}
	@Autowired
	public void setRoleServ(IRoleServ roleServ) {
		this.roleServ = roleServ;
	}
	
	public IUserDealerServ getUserDealerServ() {
		return userDealerServ;
	}
	@Autowired
	public void setUserDealerServ(IUserDealerServ userDealerServ) {
		this.userDealerServ = userDealerServ;
	}
	public IUserRoleServ getUserRoleServ() {
		return userRoleServ;
	}
	@Autowired
	public void setUserRoleServ(IUserRoleServ userRoleServ) {
		this.userRoleServ = userRoleServ;
	}
	public UserMapper getUserMapper() {
		return userMapper;
	}
	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public User selectUserExt(String uid) {
		// TODO Auto-generated method stub
		return getUserMapper().selectUserExtByUid(uid);
	}
	@Override
	public User selectUser(String uid) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(uid);
	}
	@Override
	//@Transactional(propagation = Propagation.REQUIRED,readOnly=true,isolation=Isolation.DEFAULT) 
	@Transactional(propagation = Propagation.REQUIRED) 
	public void updateUserLastInfo(String principal,String ip) {
		// TODO Auto-generated method stub
		User user = userMapper.selectUserByUserName(principal);
		User subject = new User();
		subject.setUserId(user.getUserId());
		subject.setLastLoginTime(DataTimeUtil.getCurrentTime());
		subject.setLastLoginIp(ip);
		userMapper.updateByPrimaryKeySelective(subject);
	}
	/**
	 * 管理员列表
	 */
	@Override
	public List<User> getUserExtList(User user) {
		user.computeStart();
		return userMapper.selectUserExtList(user.getStart(), user.getRows());
	}
	/**
	 * 获得全部管理员数量
	 */
	@Override
	public int getUserExtListCount() {
		return userMapper.selectUserExtListCount();
	} 
	/**
	 * 获得全部管理员
	 */
	@Override
	public List<User> selectAllUserList(User user){
		user.computeStart();
		return userMapper.selectAllUserList(user);
	}
	/**
	 * 获得全部管理员数量
	 */
	@Override
	public int getAllUserCount() {
		// TODO Auto-generated method stub
		return userMapper.selectAllUserCount();
	}
	
	/**
	 * 更新管理是否锁定
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateUserLocked(String flag, String[] userIds) {
		// TODO Auto-generated method stub
		if(userIds != null && userIds.length > 0 && (flag.equals("1") || flag.equals("0"))){
			return userMapper.updateUserLocked(flag, userIds);
		}
		return 0;
	}
	/**
	 * 通过用户名查找用户
	 */
	@Override
	public User getUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return userMapper.selectUserByUserName(userName);
	}
	/**
	 * 新建管理员
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertUser(User user, String roleIds) throws BusinessException {
		int status = userMapper.insertSelective(user);
		if(status > 0){
			if(!roleIds.isEmpty()){//从页面获取赋值的角色
				String[] pids = roleIds.split(",");
				List<String> roles = Arrays.asList(pids);
				for(String roleId : roles){
						UserRole ur = new UserRole();
						ur.setUserId(user.getUserId());
						ur.setRoleId(roleId);
						userRoleServ.insertSelective(ur);
				}
			}
		}else{
			throw new BusinessException(ShopExceptionConstant.FAILURE,"新建账号失败");
		}
		return status;
	}
	/**
	 * 更新管理员
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateUser(User user,String roleIds) throws BusinessException {
		// TODO Auto-generated method stub
		User valiUser = userMapper.selectByPrimaryKey(user.getUserId());
		int status =0;
		if(valiUser != null){
			status = userMapper.updateByPrimaryKeySelective(user);
			if(status > 0){
				userRoleServ.deleteRole(user.getUserId());//删除原有角色，重新插入角色
				if(!roleIds.isEmpty()){//从页面获取赋值的角色
					String[] pids = roleIds.split(",");
					List<String> roles = Arrays.asList(pids);
					for(String roleId : roles){
							UserRole ur = new UserRole();
							ur.setUserId(user.getUserId());
							ur.setRoleId(roleId);
							userRoleServ.insertSelective(ur);
					}
				}else{
					throw new BusinessException(ShopExceptionConstant.ERROR,"角色不存在");
				}
			}else{
				throw new BusinessException(ShopExceptionConstant.FAILURE,"管理员更新失败");
			}
		}else{
			throw new BusinessException(ShopExceptionConstant.ERROR,"管理员不存在");
		}
		return status;
	}
	/**
	 * 删除管理员
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteUser(String userId) throws BusinessException {
		// TODO Auto-generated method stub
		User valiUser = userMapper.selectByPrimaryKey(userId);
		if(valiUser == null){
			throw new BusinessException(ShopExceptionConstant.ERROR,"管理员不存在");
		}
		int status = userMapper.deleteByPrimaryKey(userId);
		if(status > 0){
			userRoleServ.deleteRole(userId);
//			userDealerServ.deleteDealer(userId);
		}
		return status;
	}
	/**
	 * 获得当前用户信息
	 */
	@Override
	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return userMapper.selectUserByUserName((String)SecurityUtils.getSubject().getPrincipal());
	}
	@Override
	public int updateUserById(User user) {
		 
		return userMapper.updateByPrimaryKey(user);
	}
}
