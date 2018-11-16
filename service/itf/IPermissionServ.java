/**
 * 找化客电商平台
 *
 * 2015 化仙子电子商务有限公司-版权所有
 */
package com.shop.service.itf;

import java.util.List;

import com.shop.dao.entity.Permission;

/**
 * 作     者：宋怀龙
 * 
 * 说     明：权限管理
 * 
 * 创建时间：2015-11-17
 */
public interface IPermissionServ {
	List<Permission> getAllPermission();
	int selectAllPermissionCount();
	int getAllPermissionCount();
	Permission getPermission(String pid);
	Permission getPermissionByCode(String permissionCode);
	int insertPermission(Permission permissions);
	int updatePermission(Permission permissions);
}
