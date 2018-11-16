package com.shop.service.itf;

import java.util.List;
import java.util.Map;

import com.shop.controller.model.MgrMatchOrderModel;
import com.shop.dao.entity.BuyMatchOrder;
import com.shop.dao.entity.OrderAddress;
import com.shop.dao.entity.SellMatchOrder;

/**
 * 作     者：宋怀龙
 * 
 * 说     明：撮合订单管理接口类
 * 
 * 创建时间：2015-11-19
 */
public interface IMatchOrderServ {
	
	/**
	  * 方法描述：获取所有买家待撮合订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:05:47
	  */
	List<BuyMatchOrder> selectBuyMatchOrderList(BuyMatchOrder buyMatchOrder,String param);
	int getAllBuyMatchOrderCount(BuyMatchOrder buyMatchOrder, String param) ;
	/**
	  * 方法描述：获取所有卖家待撮合订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:06:09
	  */
	List<SellMatchOrder> selectSellMatchOrderList(SellMatchOrder sellMatchOrder,String productName);
	int getAllSellMatchOrderCount(SellMatchOrder smo, String param) ;
	
	SellMatchOrder getSellMatchOrderById(Long smoid);
	
	BuyMatchOrder getBuyMatchOrderById(Long bmoid);
	
	/**
	  * 方法描述：根据撮合订单号修改购买撮合订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-20 下午3:54:01
	  */
	int updateBuyMatchOrderById(MgrMatchOrderModel matchOrderModel);
	
	/**
	  * 方法描述：根据撮合订单号修改销售撮合订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-12-1 下午4:56:29
	  */
	public int updateSellMatchOrderById(MgrMatchOrderModel matchOrderModel );
	
	/**
	  * 方法描述：撮合待撮合购买需求订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-28 下午3:03:52
	  */
	public int clichBuyMatchOrderById(BuyMatchOrder buyMatchOrder,boolean isClinchOver,String payMethod);
	/**
	  * 方法描述：撮合待撮合销售需求订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-28 下午3:03:52
	  */
	public int clichSellMatchOrderById(SellMatchOrder sellMatchOrder,boolean isClinchOver,String payMethod);
	/**
	  * 方法描述：根据撮合订单号修改销售撮合订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-20 下午3:54:01
	  */
	int updateSellMatchOrderById(SellMatchOrder SellMatchOrder);
	
	List<Map<String,String>> getAddressProvince();
	List<Map<String,String>> getAddressCity(Integer provinceId);
	int updateAddressById(OrderAddress address);
	
	/*供前台使用接口*****/
	
	/**
	  * 方法描述：新增报价，插入到BuyMatchOrder表
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-18 下午2:11:25
	  */
	int insertBuyMatchOrder(MgrMatchOrderModel matchOrderModel);
	/**
	  * 方法描述：新增询价，插入到SellMatchOrder表
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-18 下午2:11:25
	  */
	int insertSellMatchOrder(MgrMatchOrderModel matchOrderModel);
}
