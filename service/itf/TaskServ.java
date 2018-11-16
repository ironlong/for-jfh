package com.shop.service.itf;

import java.util.List;

import com.shop.dao.entity.BuyGoods;
import com.shop.dao.entity.Order;
import com.shop.dao.entity.SellGoods;


	/**
	 * 类描述：定时任务接口
 	 * @version: 1.0
	 * @author: Administrator
	 * @version: 2016-2-3 上午10:24:41 
	 */
public interface TaskServ {
 
	
	/**
	  * 方法描述：查询所有未付款订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-2-3 下午4:37:25
	  */
	List<Order>  selectOrderOutPayTask();
 
	
	/**
	  * 方法描述：查询所有已审核已上架未过期的求购需求列表
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-2-3 下午5:17:00
	  */
	List<BuyGoods> selectBuyTask();
	/**
	  * 方法描述：查询所有已审核已上架未过期的销售需求列表
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-2-3 下午5:17:00
	  */
	List<SellGoods> selectSellTask();
 
}
