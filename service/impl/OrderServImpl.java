package com.shop.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.exception.BusinessException;
import com.shop.common.exception.ShopExceptionConstant;
import com.shop.common.utils.CommonDatas;
import com.shop.common.utils.DataTimeUtil;
import com.shop.common.utils.SendSMS;
import com.shop.common.utils.SerialNumberUtil;
import com.shop.common.utils.SystemConfig;
import com.shop.controller.model.FrontOrderNotifyModel;
import com.shop.controller.model.MgrOrderModel;
import com.shop.dao.entity.Address;
import com.shop.dao.entity.AddressDb;
import com.shop.dao.entity.Bank;
import com.shop.dao.entity.Order;
import com.shop.dao.entity.OrderAddress;
import com.shop.dao.entity.OrderDelivery;
import com.shop.dao.entity.OrderExtrapay;
import com.shop.dao.entity.OrderMatchinf;
import com.shop.dao.entity.OrderNotify;
import com.shop.dao.entity.OrderRefund;
import com.shop.dao.entity.Users;
import com.shop.dao.itf.AddressDbMapper;
import com.shop.dao.itf.AddressMapper;
import com.shop.dao.itf.BankMapper;
import com.shop.dao.itf.OrderAddressMapper;
import com.shop.dao.itf.OrderDelMapper;
import com.shop.dao.itf.OrderDeliveryMapper;
import com.shop.dao.itf.OrderExtrapayMapper;
import com.shop.dao.itf.OrderMapper;
import com.shop.dao.itf.OrderMatchinfMapper;
import com.shop.dao.itf.OrderNotifyMapper;
import com.shop.dao.itf.OrderRefundMapper;
import com.shop.service.itf.IOrderServ;
import com.shop.service.itf.ISellGoodsServ;
import com.shop.service.itf.IUsersServ;
@Service("OrderServImpl")
public class OrderServImpl implements IOrderServ {
	OrderMapper orderMapper;
	OrderDelMapper orderDelMapper;
	OrderDeliveryMapper orderDeliveryMapper;
	OrderExtrapayMapper orderExtrapayMapper;
	OrderRefundMapper orderRefundMapper;
	AddressMapper addressMapper;
	AddressDbMapper addressDbMapper;
	OrderAddressMapper orderAddressMapper;
	IUsersServ UsersServ;
	ISellGoodsServ sellgoodsServ;
	BankMapper bankMapper;
	OrderNotifyMapper orderNotifyMapper;
	OrderMatchinfMapper orderMatchinfMapper;
	@Override
	public List<Order> selectStateOrderList(Order order,
			String productName) {
		order.computeStart();
		return orderMapper.selectStateOrderList(order,productName);
	}

	@Override
	public int getStateOrderCount(Order order,
			String productName) {
		// TODO Auto-generated method stub
		return orderMapper.getStateOrderCount(order,productName);
	}

	public OrderMapper getOrderMapper() {
		return orderMapper;
	}
	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	@Override
	public Order getOrderById(Long orderId) {
		
		return orderMapper.selectByPrimaryKey(orderId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateOrderById(MgrOrderModel orderModel) {
		Order order = orderMapper.selectByPrimaryKey(orderModel.getOrderId());
		order.setOperatorRemark(orderModel.getOperatorRemark());//订单只允许后台修改少量字段
		
		
		return orderMapper.updateByPrimaryKey(order);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateOrderState(MgrOrderModel orderModel) throws UnsupportedEncodingException {
		Order order = orderMapper.selectOrderById(orderModel.getOrderId());
		
		if(orderModel.getOrderState().equals(SystemConfig.ORDERSTATE_CLOSE)){//如果是关闭订单操作，则把关闭前状态储存一下
			order.setBeforeCloseState(order.getOrderState());
			order.setOrderState(orderModel.getOrderState());
		}else if(orderModel.getOrderState().equals(SystemConfig.ORDERSTATE_ACTIVE)){//如果是激活订单操作，则把关闭前的状态恢复
			order.setOrderState(order.getBeforeCloseState());
		}else if(orderModel.getOrderState().equals(SystemConfig.ORDERSTATE_NOSETTLEMENT)){//如果是变更为待结算操作，则进行实际总价格、实际吨数计算
			//计算实际价格、实际吨数计算
			calculateDileveryOverOrder(order);
			order.setOrderState(orderModel.getOrderState());
		}else if(orderModel.getOrderState().equals(SystemConfig.ORDERSTATE_PAYED)){//如果是审核成已付款状态，则添加审核时间
			order.setCheckTime(DataTimeUtil.getCurrentTime());
			if(order.getPayMethod().equals(SystemConfig.PAYTYPE_DOWNLINE)){//如果是线下交易，则直接进入已完成
				order.setOrderState(SystemConfig.ORDERSTATE_OVER);
			}else{
				order.setOrderState(orderModel.getOrderState());
			}
		}else{
			order.setOrderState(orderModel.getOrderState());
		}
		int result = orderMapper.updateByPrimaryKey(order);
		
		//订单付款审核完毕，给买卖双方发送短信通知
		if(order.getOrderState().equals(SystemConfig.ORDERSTATE_PAYED)){
			//给买家发送短信
			SendSMS.SEND_ALTERORDER_STATE(order.getBuyerAddr().getMobile(),order.getOrderCode(),"已付款");
			//给卖家发送短信
			SendSMS.SEND_ALTERORDER_STATE(order.getSellerAddr().getMobile(),order.getOrderCode(),"已付款");
		}
		return result;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateOrder(Order order) {
		return orderMapper.updateByPrimaryKey(order);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED) 
	public int updateOrderSelective(Order order) {
		return orderMapper.updateByPrimaryKeySelective(order);
	}
	/**
	  * 方法描述：计算提货完成的订单实际总价格、实际吨数
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2015-12-12 下午3:59:42
	  */
	public void calculateDileveryOverOrder(Order order){
		BigDecimal sum = orderDeliveryMapper.getOrderDeliverySum(order.getOrderId(),SystemConfig.ORDER_ISSETTLE_YES);
		order.setRealWeight(sum);
		order.setRealMoneyTotal(sum.multiply(order.getSingleprice()));
	}
	@Override
	public List<OrderDelivery> selectOrderDeliveryList(
			OrderDelivery orderDelivery) {
		
		return orderDeliveryMapper.selectOrderDeliveryList(orderDelivery);
	}

	@Override
	public int getOrderDeliveryCount(OrderDelivery orderDelivery) {
		// TODO Auto-generated method stub
		return orderDeliveryMapper.getOrderDeliveryCount(orderDelivery);
	}

	public OrderDeliveryMapper getOrderDeliveryMapper() {
		return orderDeliveryMapper;
	}
	@Autowired
	public void setOrderDeliveryMapper(OrderDeliveryMapper orderDeliveryMapper) {
		this.orderDeliveryMapper = orderDeliveryMapper;
	}

	@Override
	public OrderDelivery selectOrderDeliveryById(Long orderDeliveryId) {
		// TODO Auto-generated method stub
		return orderDeliveryMapper.selectByPrimaryKey(orderDeliveryId);
	}

	@Override
	public int updateOrderDelivery(OrderDelivery orderDelivery) {
		return orderDeliveryMapper.updateByPrimaryKey(orderDelivery);
	}

	@Override
	public List<OrderExtrapay> selectExtraOrderList(Order order) {
		
		return orderExtrapayMapper.selectExtraOrderListByOrderId(order.getOrderId());
	}

	@Override
	public List<OrderRefund> selectRefundOrderList(Order order) {
		
		return orderRefundMapper.selectRefundOrderListByOrderId(order.getOrderId());
	}

	public OrderExtrapayMapper getOrderExtrapayMapper() {
		return orderExtrapayMapper;
	}
	@Autowired
	public void setOrderExtrapayMapper(OrderExtrapayMapper orderExtrapayMapper) {
		this.orderExtrapayMapper = orderExtrapayMapper;
	}

	public OrderRefundMapper getOrderRefundMapper() {
		return orderRefundMapper;
	}
	@Autowired
	public void setOrderRefundMapper(OrderRefundMapper orderRefundMapper) {
		this.orderRefundMapper = orderRefundMapper;
	}

	@Override
	public int updateOrderExtrapayState(OrderExtrapay orderExtrapay) {
		OrderExtrapay oe = orderExtrapayMapper.selectByPrimaryKey(orderExtrapay.getExtrapayId());
		oe.setAuthState(orderExtrapay.getAuthState());
		oe.setOperator(orderExtrapay.getOperator());
		
		return orderExtrapayMapper.updateByPrimaryKey(oe);
	}

	@Override
	public List<Order> selectOrderListForIndex(Order order, String orderState) {
		order.setRows(CommonDatas.orderNum);
		
		return orderMapper.selectOrderListForIndex(order,orderState);
	}

	@Override
	public List<Order> selectMyBOrderList(Long usersId,int min,int max,String orderState){
		// TODO Auto-generated method stub
		return orderMapper.selectMyBOrderList(usersId, min, max,  orderState);
	}

	@Override
	public int countSelectMyBOrderList(Long usersId,String orderState) {
		// TODO Auto-generated method stub
		return orderMapper.countSelectMyBOrderList(usersId,orderState);
	}
	@Override
	public List<Order> selectMySOrderList(Long usersId,int min,int max,String orderState){
		// TODO Auto-generated method stub
		return orderMapper.selectMySOrderList(usersId, min, max,orderState);
	}

	@Override
	public int countSelectMySOrderList(Long usersId,String orderState) {
		// TODO Auto-generated method stub
		return orderMapper.countSelectMySOrderList(usersId,orderState);
	}

	@Override
	public int delOrderById(Long orderId) {
		orderDelMapper.insertByOrderId(orderId);
		
		return orderMapper.deleteByPrimaryKey(orderId);
	}

	
	public OrderDelMapper getOrderDelMapper() {
		return orderDelMapper;
	}
	@Autowired
	public void setOrderDelMapper(OrderDelMapper orderDelMapper) {
		this.orderDelMapper = orderDelMapper;
	}

	@Override
	public List<Map<String, String>> selectOrderNumByUsersId(Long usersId) {
		
		return orderMapper.selectOrderNumByUsersId(usersId);
	}

	@Override
	public Order selectOrderById(Long orderId) {
		return orderMapper.selectOrderById(orderId);
	}

	@Override
	public int insertFastOrder(Order order) {
		//新增订单地址子表
		Address address =addressMapper.selectByPrimaryKey(order.getFkBuyerAddrId());
	    OrderAddress orderAddress = new OrderAddress();
	    orderAddress.setFkUsersId(address.getFkUsersId());
	    orderAddress.setProvince(address.getProvince());
	    orderAddress.setCity(address.getCity());
	    orderAddress.setDetail(address.getDetail());
	    orderAddress.setMobile(address.getMobile());
	    orderAddress.setLinkman(address.getLinkman());
	    orderAddress.setCreateTime(DataTimeUtil.getCurrentTime());
	    orderAddressMapper.insertAddr(orderAddress); 
	    order.setFkBuyerAddrId(orderAddress.getAddressId());//重新set订单的买家地址
		return orderMapper.insertGetId(order);//生成待付款订单
	}

	public IUsersServ getUsersServ() {
		return UsersServ;
	}
	@Autowired
	public void setUsersServ(IUsersServ usersServ) {
		UsersServ = usersServ;
	}

	public ISellGoodsServ getSellgoodsServ() {
		return sellgoodsServ;
	}
	@Autowired
	public void setSellgoodsServ(ISellGoodsServ sellgoodsServ) {
		this.sellgoodsServ = sellgoodsServ;
	}

	@Override
	public int delAddressById(Long addressId, boolean isDelDefault) {
		return addressMapper.deleteByPrimaryKey(addressId);
	}

	public AddressMapper getAddressMapper() {
		return addressMapper;
	}
	@Autowired
	public void setAddressMapper(AddressMapper addressMapper) {
		this.addressMapper = addressMapper;
	}

	@Override
	public Address addLinmanAddress(Address address, boolean isDefault,
			Users users) {
		 AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(address.getProvince()));
		  AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(address.getCity()));
		
		
		  address.setProvince(province.getName());
		  address.setCity(city.getName());
		address.setFkUsersId(users.getUsersId());
		addressMapper.insertAddr(address);
		if(isDefault){
			users.setDefAddressId(address.getAddressId());
			UsersServ.updateByPrimaryKeyWithBLOBs(users);
		}
		return address;
	}

	public AddressDbMapper getAddressDbMapper() {
		return addressDbMapper;
	}
	@Autowired
	public void setAddressDbMapper(AddressDbMapper addressDbMapper) {
		this.addressDbMapper = addressDbMapper;
	}

	@Override
	public List<Bank> getAllBanks() {
		
		return bankMapper.selectAllBanks();
	}

	public BankMapper getBankMapper() {
		return bankMapper;
	}
	@Autowired
	public void setBankMapper(BankMapper bankMapper) {
		this.bankMapper = bankMapper;
	}

	@Override
	public Address getLinmanAddress(Long addressId) {
		 
		return addressMapper.selectByPrimaryKey(addressId);
	}

	@Override
	public int insertOrderDelivery(OrderDelivery orderDelivery) {
		return orderDeliveryMapper.insertOrderDelivery(orderDelivery);
	}

	@Override
	public OrderDelivery getOrderDeliveryById(Long orderDelivery) {
		return orderDeliveryMapper.selectByPrimaryKey(orderDelivery);
	}

	
	@Override
	public int notifyDeliveryGoods(FrontOrderNotifyModel frontOrderNotifyModel) {
		
		Order order = this.selectOrderById(frontOrderNotifyModel.getFkOrderId());
		order.setIsBuyerDelivery(SystemConfig.AGREE_DELIVERY_YES);//买家同意提货
		updateOrder(order);
		
		//添加提货申请
		OrderNotify orderNotify = new OrderNotify();
		orderNotify.setCreateTime(DataTimeUtil.getCurrentTime());
		orderNotify.setOrderNotifyCode(SerialNumberUtil.getSerialNumber());
		String expire = frontOrderNotifyModel.getExpire();
		String[] expires = expire.split("至");
		orderNotify.setDeliveryEndTime(expires[1].trim());
		orderNotify.setDeliveryStartTime(expires[0].trim());
		orderNotify.setLinkman(order.getBuyerAddr().getLinkman());
		orderNotify.setMobile(order.getBuyerAddr().getMobile());
		orderNotify.setFkOrderId(frontOrderNotifyModel.getFkOrderId());
		orderNotify.setIsExpired("1"); 
		int result = orderNotifyMapper.insert(orderNotify);
		
		//发短信通知卖方有提货申请
		
		try {
			SendSMS.SEND_SHENQINGTIHUO(order.getSellerAddr().getMobile(),order.getOrderCode());
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException(ShopExceptionConstant.FAILURE, "订单号orderCode="+order.getOrderCode()+"给卖家mobile="+order.getSellerAddr().getMobile()+"发提货申请短信通知时出错，请联系管理员");
		}
		return result;
		
	}
 
	public OrderNotifyMapper getOrderNotifyMapper() {
		return orderNotifyMapper;
	}
	@Autowired
	public void setOrderNotifyMapper(OrderNotifyMapper orderNotifyMapper) {
		this.orderNotifyMapper = orderNotifyMapper;
	}

	@Override
	public int insertOrderExtrapay(OrderExtrapay orderExtrapay) {
		return orderExtrapayMapper.insert(orderExtrapay);
	}

	@Override
	public int insertOrderRefund(OrderRefund orderRefund) {
		return orderRefundMapper.insert(orderRefund);
	}

	public OrderMatchinfMapper getOrderMatchinfMapper() {
		return orderMatchinfMapper;
	}
	@Autowired
	public void setOrderMatchinfMapper(OrderMatchinfMapper orderMatchinfMapper) {
		this.orderMatchinfMapper = orderMatchinfMapper;
	}

	@Override
	public int insertMatchInfo(OrderMatchinf orderMatchinf) {
		 
		return orderMatchinfMapper.insertGetId(orderMatchinf);
	}

	@Override
	public OrderMatchinf getOrderMatchinfByOrderId(Long orderId) {
		return orderMatchinfMapper.selectByOrderId(orderId);
	}

	public OrderAddressMapper getOrderAddressMapper() {
		return orderAddressMapper;
	}
	@Autowired
	public void setOrderAddressMapper(OrderAddressMapper orderAddressMapper) {
		this.orderAddressMapper = orderAddressMapper;
	}
}
