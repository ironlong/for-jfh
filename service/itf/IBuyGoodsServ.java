package com.shop.service.itf;

import java.util.List;
import java.util.Map;

import com.shop.controller.model.MgrGoodsModel;
import com.shop.dao.entity.BuyGoods;
import com.shop.dao.entity.OrderAddress;
import com.shop.dao.entity.Users;

public interface IBuyGoodsServ {
	BuyGoods getBuyGoodsById(Long buyGoodId);
	int updateBuyGoods(BuyGoods bgoods);
	int deleteBuyGoodsById(Long buyGoodId);
	int updateBuyGoodsById(BuyGoods buyGoods);
	BuyGoods selectByPrimaryKey(Long buyGoodId);
	int updateByPrimaryKeyWithBLOBs(BuyGoods buyGoods);
	int updateByPrimaryKey(BuyGoods bgs);
	int updateById(BuyGoods bgs);
	int insert(BuyGoods buyGoods);
	BuyGoods selectByFkId(long parseLong);
	List<Map<String, String>> getAddressProvince();
	List<Map<String, String>> getAddressCity(Integer provinceId);
	int updateAddressById(OrderAddress address);
	int updateBuyGoodById(MgrGoodsModel buyGoodsModel);
	int updateUsersById(Users users);
	/*
	 * 前台使用标识 ↓
	 * */
	//获得产品全部数量
	int getAllBuyGoodsCount();
	//获得全部产品信息
	List<BuyGoods> selectAllBuyGoodsList(BuyGoods bgoods, String param);
	//获得当前产品信息
	BuyGoods getCurrentBuyGoods();
	//后台发布求购信息
	int insert(MgrGoodsModel buyGoodsModel);
	
	/**
	  * 方法描述：前台发布求购信息
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2016-1-15 上午10:14:12
	  */
	int insertFront(MgrGoodsModel buyGoodsModel);
	//首页显示5条求购信息
	List<BuyGoods> selectBuyGoodsListForIndex(BuyGoods buyGoods,byte dstat);
	//产品列表展示
	List<BuyGoods> selectBuyGoodsListByName(BuyGoods buyGoods,byte dstat,String productName);
	List<BuyGoods> selectAllBuyGoods(int min,int max);
	BuyGoods selectBuyGoodsById(Long buyGoodId);
	/**
	  * 方法描述：提供给头部搜索框
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: lenovo
	  * @version: 2016-1-12 上午10:18:14
	  */
	List<BuyGoods> searchList(String para,int min,int max);
	int getsearchListCount(String para,int min,int max);
	// 条件查询 
	List<BuyGoods> selectBuyGoodsByCondition(Map<String, Object> params);
	// 条件查询  总记录数
	int getGoodsByConditionCount(Map<String, Object> params);
	List<BuyGoods> selectMyBuysList(Long usersId, int min, int max,
			String dstat);
	int countSelectMyBuysList(Long usersId, String dstat);
}
