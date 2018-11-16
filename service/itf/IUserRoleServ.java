 
package com.shop.service.itf;

import java.util.List;

import com.shop.dao.entity.Node;
import com.shop.dao.entity.UserRole;

/**
 * 
 * 说     明：
 * 
 * 创建时间：2014-4-23
 */
public interface IUserRoleServ {
	
	int insertSelective(UserRole record);
	int insert(UserRole record);
	int deleteRole(String userId);
	
	
	/**
	  * 方法描述：根据用户ID获取该用户拥有的唯一角色
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-12 下午7:36:04
	  */
	UserRole getRoleUserByUserId(String userId);
	/**
	  * 方法描述：根据用户名获取菜单列表（过滤权限后）
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-4 下午7:39:38
	  */
	List<Node> listNodeByUserId(String userId);
}
