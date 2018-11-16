package com.shop.service.itf;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.shop.controller.model.FrontOrderNotifyModel;
import com.shop.controller.model.MgrOrderModel;
import com.shop.dao.entity.Address;
import com.shop.dao.entity.Bank;
import com.shop.dao.entity.Order;
import com.shop.dao.entity.OrderDelivery;
import com.shop.dao.entity.OrderExtrapay;
import com.shop.dao.entity.OrderMatchinf;
import com.shop.dao.entity.OrderRefund;
import com.shop.dao.entity.Users;

public interface IOrderServ {
	/**
	  * 方法描述：获取所有待付款订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:05:47
	  */
	List<Order> selectStateOrderList(Order order,String productName);
	int getStateOrderCount(Order order,
			String productName);
	
	/**
	  * 方法描述：获取所有提货单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:05:47
	  */
	List<OrderDelivery> selectOrderDeliveryList(OrderDelivery orderDelivery);
	int getOrderDeliveryCount(OrderDelivery orderDelivery);
	
	OrderDelivery getOrderDeliveryById(Long orderDelivery);
	
	int updateOrder(Order order); 
	int updateOrderSelective(Order order);
	Order getOrderById(Long orderId);
	
	int updateOrderDelivery(OrderDelivery orderDelivery) ;
	OrderDelivery selectOrderDeliveryById(Long orderDeliveryId);
	
	int insertOrderDelivery(OrderDelivery orderDelivery) ;

	/**
	  * 方法描述：根据撮合订单号修改购买撮合订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-11-20 下午3:54:01
	  */
	int updateOrderById(MgrOrderModel orderModel);
	int updateOrderState(MgrOrderModel orderModel) throws UnsupportedEncodingException;
	
	/**
	  * 方法描述：根据订单号获取所有补款单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-12-15下午2:05:47
	  */
	List<OrderExtrapay> selectExtraOrderList(Order order);
	int insertOrderExtrapay(OrderExtrapay orderExtrapay);
	int insertOrderRefund(OrderRefund orderRefund);
	/**
	  * 方法描述：根据订单号获取所有退款单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-12-15下午2:05:47
	  */
	List<OrderRefund> selectRefundOrderList(Order order);
	
	int updateOrderExtrapayState(OrderExtrapay orderExtrapay);
	
	//////////////////////////供前台使用接口////////////////////////
	/**
	  * 方法描述：获取前台首页滚动待付款订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:05:47
	  */
	List<Order> selectOrderListForIndex(Order order,String orderState);
	
	/**
	 * 根据用户ID查询用户购买订单列表
	 */
	List<Order> selectMyBOrderList(Long usersId,int min,int max,String orderState);
	int countSelectMyBOrderList(Long usersId,String orderState);
	/**
	 * 根据用户ID查询用户销售订单列表
	 */
	List<Order> selectMySOrderList(Long usersId,int min,int max,String orderState);
	int countSelectMySOrderList(Long usersId,String orderState);
	
	
	/**
	  * 方法描述：根据订单ID删除订单表记录，并在删除表中添加一条备份记录
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-4 下午8:40:56
	  */
	int delOrderById(Long orderId);
	
	
	/**
	  * 方法描述：提供给个人中心展示订单数量
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-5 上午9:41:50
	  */
	List<Map<String, String>> selectOrderNumByUsersId(Long usersId);
	
	
	/**
	  * 方法描述：根据订单ID查询订单详情
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-5 下午4:26:43
	  */
	Order selectOrderById(Long orderId);
	
	
	/**
	  * 方法描述：添加直接下单的订单
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-6 下午5:19:10
	  */
	int insertFastOrder(Order order);
	
	/**
	  * 方法描述：添加订单与需求关联信息
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-27 下午5:53:09
	  */
	int insertMatchInfo(OrderMatchinf orderMatchinf);
	OrderMatchinf getOrderMatchinfByOrderId(Long orderId);
	/**
	  * 方法描述：删除联系人地址
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-7 下午2:56:27
	  */
	int delAddressById(Long addressId,boolean isDelDefault);
	
	
	/**
	  * 方法描述：添加联系人地址
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-7 下午4:42:46
	  */
	Address addLinmanAddress(Address address,boolean isDelDefault,Users users);
	Address getLinmanAddress(Long addressId);
	List<Bank> getAllBanks();
	
	
	/**
	  * 方法描述：买家通知卖家提货
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-19 下午3:12:20
	  */
	int notifyDeliveryGoods(FrontOrderNotifyModel frontOrderNotifyModel);
}
