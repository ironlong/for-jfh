package com.shop.service.itf;

import java.util.List;

import com.shop.common.exception.BusinessException;
import com.shop.dao.entity.User;

public interface IUserServ {
	User selectUserExt(String uid);
	User selectUser(String uid);
	User getUserByUserName(String userName);
	void updateUserLastInfo(String principal,String ip);
	List<User> getUserExtList(User user);
	int getUserExtListCount();
	
	
	/**
	  * 方法描述：获取所有用户列表
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:11:00
	  */
	List<User> selectAllUserList(User user);
	int getAllUserCount();
	int updateUserLocked(String flag, String[] userIds) ;
	int insertUser(User user,String roleId) throws BusinessException;
	int updateUser(User user,String roleId) throws BusinessException;
	int updateUserById(User user);
	int deleteUser(String userId) throws BusinessException;
	User getCurrentUser();
}
