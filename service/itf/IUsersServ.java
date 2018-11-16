package com.shop.service.itf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shop.dao.entity.Users;

public interface IUsersServ {

	int getUsersCount();
	List<Users> getUsers(Users users);

	int deleteByIds(Long usersId);

	int insert(HttpServletRequest request);

	Users selectByPrimaryKey(Long usersId);

	int updateByPrimaryKeyWithBLOBs(Users users);

	int buildQueue(Users users, String groupId);

	List<Users> getUsersByInput(String usersId);
	
	Users getUsersByUsersName(String usersName);
	int insertUsers(Users users);
	/**
	  * 方法描述：根据用户ID获取带用户地址列表的用户信息
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-7 上午10:49:59
	  */
	Users getUsersDetailByUsersId(Long usersId);
	List<Map<String, String>> getAddressProvince();
	List<Map<String, String>> getAddressCity(int parseInt);
}
