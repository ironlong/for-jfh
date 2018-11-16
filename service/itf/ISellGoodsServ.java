package com.shop.service.itf;

import java.util.List;
import java.util.Map;

import com.shop.controller.model.MgrGoodsModel;
import com.shop.dao.entity.OrderAddress;
import com.shop.dao.entity.SellGoods;
import com.shop.dao.entity.Users;

public interface ISellGoodsServ {
	
	SellGoods getSellGoodsById(Long sellGoodsId);
	int insert(SellGoods sellGoods);
	int updateSellGoods(SellGoods sgoods);
	int updateByPrimaryKey(SellGoods sgs);
	int deleteSellGoodsById(Long sellGoodsId);
	int updateSellGoodsById(SellGoods sgs);
	int updateById(SellGoods sgs);
	int updateByPrimaryKeyWithBLOBs(SellGoods sellGoods);
	int updateSellGoodById(MgrGoodsModel sellGoodsModel);
	List<Map<String, String>> getAddressProvince();
	List<Map<String, String>> getAddressCity(Integer provinceId);
	int updateAddressById(OrderAddress address);
	int updateUsersById(Users users);
	int insert(MgrGoodsModel sellGoodsModel);
	/*
	 * 前台使用标识 ↓
	 * */
	//获得产品全部数量
	int getAllSellGoodsCount();
	//获得全部产品信息
	List<SellGoods> selectAllSellGoodsList(SellGoods sgoods, String param);
	//获得当前产品信息
	SellGoods getCurrentSellGoods(Long sellGoodsId);
	//首页显示的销售信息
	List<SellGoods> selectSellGoodsListForIndex(SellGoods sellGoods, Byte dstat,
			String productName);
	//销售信息列表
	List<SellGoods> selectSellGoodsList(SellGoods sellGoods, Byte dstat,
			String productName);
	int selectSellGoodsListCount(SellGoods sellGoods, Byte dstat,
				String productName);
	//一条销售信息    
	SellGoods selectSellGoods(String sellGoodsId);
	SellGoods selectSellGoodsById1(String sellGoodsId);
	
	List<SellGoods> selectAllSellGoods(int page, int pageSize);
	/**
	  * 方法描述：提供给头部搜索框
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: lenovo
	  * @version: 2016-1-12 上午10:18:14
	  */
	List<SellGoods> searchList(String para,int min,int max);
	int getsearchListCount(String para,int min,int max);
	int insertFront(MgrGoodsModel sellGoodsModel);
	
	// 条件查询 
		List<SellGoods> selectSellGoodsByCondition(Map<String, Object> params);
		// 条件查询  总记录数
		int getGoodsByConditionCount(Map<String, Object> params);
		
	List<SellGoods> selectMySellsList(Long usersId, int min, int max,
			String dstat);
	int countSelectMySellsList(Long usersId, String dstat);
		
}
