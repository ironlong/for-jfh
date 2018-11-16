/**
 * 用友商城
 *
 * 2014 用友软件股份有限公司-版权所有
 */
package com.shop.service.itf;

import java.util.List;

/**
 * 
 * 说     明：
 * 
 * 创建时间：2014-4-23
 */
public interface IUserDealerServ {
	int batchInsertUserDealer(String userId,String[] dealerIds);
	int deleteDealer(String userId);
	int batchDeleteDealer(String dealerId);
	List<String> getDealerIdsByUserId(String userId);
}
