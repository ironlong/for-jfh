package com.shop.service.impl;

 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.BuyGoods;
import com.shop.dao.entity.Order;
import com.shop.dao.entity.SellGoods;
import com.shop.dao.itf.BuyGoodsMapper;
import com.shop.dao.itf.OrderMapper;
import com.shop.dao.itf.SellGoodsMapper;
import com.shop.service.itf.TaskServ;

 
@Service("TaskServImpl")
public class TaskServImpl implements TaskServ{
	BuyGoodsMapper buyGoodsMapper;
	SellGoodsMapper sellGoodsMapper;
	OrderMapper orderMapper;
	public BuyGoodsMapper getBuyGoodsMapper() {
		return buyGoodsMapper;
	}
	@Autowired
	public void setBuyGoodsMapper(BuyGoodsMapper buyGoodsMapper) {
		this.buyGoodsMapper = buyGoodsMapper;
	}
	public SellGoodsMapper getSellGoodsMapper() {
		return sellGoodsMapper;
	}
	@Autowired
	public void setSellGoodsMapper(SellGoodsMapper sellGoodsMapper) {
		this.sellGoodsMapper = sellGoodsMapper;
	}
	
	/**  发布信息定时任务Start  */
	@Override
	public List<BuyGoods> selectBuyTask() {
		// TODO Auto-generated method stub
		return buyGoodsMapper.selectBuyTask();
	}
	@Override
	public List<SellGoods> selectSellTask() {
		// TODO Auto-generated method stub
		return sellGoodsMapper.selectSellTask();
	}
	
	/*  发布信息定时任务End  */

	
	/**  订单定时任务Start  */
	@Override
	public  List<Order> selectOrderOutPayTask() {
		return orderMapper.selectOrderOutPayTask();
		
	}
	public OrderMapper getOrderMapper() {
		return orderMapper;
	}
	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}
	
	/*  订单定时任务Start  */
}
