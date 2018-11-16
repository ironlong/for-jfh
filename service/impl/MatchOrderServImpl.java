/**
  * 文件名：MatchOrderServImpl.java
  * 版本信息：Version 1.0
  * 日期：2015-11-19
  * Copyright 找化客  2015 
  * 版权所有
  */
package com.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.utils.DataTimeUtil;
import com.shop.common.utils.SerialNumberUtil;
import com.shop.common.utils.SystemConfig;
import com.shop.controller.model.MgrMatchOrderModel;
import com.shop.dao.entity.Address;
import com.shop.dao.entity.AddressDb;
import com.shop.dao.entity.BuyGoods;
import com.shop.dao.entity.BuyMatchOrder;
import com.shop.dao.entity.Order;
import com.shop.dao.entity.OrderAddress;
import com.shop.dao.entity.OrderMatchinf;
import com.shop.dao.entity.SellGoods;
import com.shop.dao.entity.SellMatchOrder;
import com.shop.dao.entity.Users;
import com.shop.dao.itf.AddressDbMapper;
import com.shop.dao.itf.AddressMapper;
import com.shop.dao.itf.BuyGoodsMapper;
import com.shop.dao.itf.BuyMatchOrderMapper;
import com.shop.dao.itf.OrderAddressMapper;
import com.shop.dao.itf.OrderMapper;
import com.shop.dao.itf.OrderMatchinfMapper;
import com.shop.dao.itf.SellGoodsMapper;
import com.shop.dao.itf.SellMatchOrderMapper;
import com.shop.service.itf.IBuyGoodsServ;
import com.shop.service.itf.IMatchOrderServ;
import com.shop.service.itf.ISellGoodsServ;
import com.shop.service.itf.IUsersServ;


	/**
 * 类描述：待撮合订单业务实现类
 * @version: 1.0
 * @author: song
 * @version: 2015-11-19 下午2:07:33 
 */
@Service("MatchOrderServImpl")
public class MatchOrderServImpl implements IMatchOrderServ{
	BuyGoodsMapper buyGoodsMapper;
	SellGoodsMapper sellGoodsMapper;
	BuyMatchOrderMapper buyMatchOrderMapper;
	SellMatchOrderMapper sellMatchOrderMapper;
	OrderMapper orderMapper;
	OrderMatchinfMapper orderMatchinfMapper;
	AddressDbMapper addressDbMapper;
	AddressMapper addressMapper;
	OrderAddressMapper orderAddressMapper;
	IUsersServ usersServ;	
	ISellGoodsServ sellgoodsServ;
	IBuyGoodsServ buygoodsServ;
		
		@Override
		public List<BuyMatchOrder> selectBuyMatchOrderList(
				BuyMatchOrder buyMatchOrder,String productName) {
			buyMatchOrder.computeStart();
			return buyMatchOrderMapper.selectBuyMatchOrderList(buyMatchOrder,productName);
		}

		@Override
		public List<SellMatchOrder> selectSellMatchOrderList(
				SellMatchOrder sellMatchOrder,String productName) {
			sellMatchOrder.computeStart();
			return sellMatchOrderMapper.selectSellMatchOrderList(sellMatchOrder,productName);
		}
		
		
		public BuyMatchOrderMapper getBuyMatchOrderMapper() {
			return buyMatchOrderMapper;
		}
		@Autowired
		public void setBuyMatchOrderMapper(BuyMatchOrderMapper buyMatchOrderMapper) {
			this.buyMatchOrderMapper = buyMatchOrderMapper;
		}
		public SellMatchOrderMapper getSellMatchOrderMapper() {
			return sellMatchOrderMapper;
		}
		@Autowired
		public void setSellMatchOrderMapper(SellMatchOrderMapper sellMatchOrderMapper) {
			this.sellMatchOrderMapper = sellMatchOrderMapper;
		}
		public AddressDbMapper getAddressDbMapper() {
			return addressDbMapper;
		}
		@Autowired
		public void setAddressDbMapper(AddressDbMapper addressDbMapper) {
			this.addressDbMapper = addressDbMapper;
		}
		@Override
		public int getAllBuyMatchOrderCount(BuyMatchOrder buyMatchOrder, String param) {
			return buyMatchOrderMapper.getAllBuyMatchOrderCount(buyMatchOrder,param);
		}
		public AddressMapper getAddressMapper() {
			return addressMapper;
		}
			@Autowired
		public void setAddressMapper(AddressMapper addressMapper) {
			this.addressMapper = addressMapper;
		}

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

		@Override
		public int getAllSellMatchOrderCount(SellMatchOrder smo, String param) {
			return sellMatchOrderMapper.getAllSellMatchOrderCount(smo,param);
		}

		@Override
		@Transactional(propagation = Propagation.REQUIRED)
		public int clichBuyMatchOrderById(BuyMatchOrder buyMatchOrder,boolean isClinchOver,String payMethod) {
			BuyGoods bg = buyGoodsMapper.selectByPrimaryKey(buyMatchOrder.getFkBuygoodsId());
			bg.setDbnum(bg.getDbnum().subtract(buyMatchOrder.getClinchNum()));//每次撮合都重新计算撮合成功后的需求数量
			if(isClinchOver){//所有需求数量撮合完成，把需求信息下架
				bg.setIsShelf(SystemConfig.INFO_OVER);//撮合成功后，需求信息状态改成已完成
				
			} 
			buyGoodsMapper.updateByPrimaryKey(bg);//更新需求上下架状态及撮合后需求数量
			
			//生成待付款订单
			Order order = new Order();
			order.setBuyerUserName(buyMatchOrder.getBuyerUsersName());
			String currentTime = DataTimeUtil.getCurrentTime();
			order.setCreateTime(currentTime);
			order.setFkBuyerAddrId(buyMatchOrder.getFkBuyerAddrId());
			order.setFkBuyerUserId(buyMatchOrder.getFkBuyerUserId());
			order.setFkProductId(buyMatchOrder.getFkProductId());
			order.setFkSellerAddrId(buyMatchOrder.getFkSellerAddrId());
			order.setFkSellerUserId(buyMatchOrder.getFkSellerUserId());
			order.setMatchType(SystemConfig.MATCH_BUY);//获取撮合订单关联表时和orderid形成联合条件，可得到一个待撮合订单ID
			order.setMoneyTotal(buyMatchOrder.getClinchNum().multiply(buyMatchOrder.getClinchPrice()));
			order.setOperator(buyMatchOrder.getOperator());
			order.setOrderCode(SerialNumberUtil.getSerialCode(SystemConfig.NC));
			order.setOrderState(SystemConfig.ORDERSTATE_NOPAY);
			order.setOrderWeight(buyMatchOrder.getClinchNum());
			order.setSellerUserName(buyMatchOrder.getSellerUsersName());
			order.setSingleprice(buyMatchOrder.getClinchPrice());
			order.setPayMethod(payMethod);
			order.setFkSettleZhkbankId(2L);//默认建设银行，有时间改成后台选择框
			orderMapper.insertGetId(order);//生成待付款订单
			
			//生成待撮合订单与订单的关联信息
			OrderMatchinf om = new OrderMatchinf();
			om.setBuyOrSell(SystemConfig.MATCH_BUY);
			om.setFkMatchorderId(buyMatchOrder.getBuyMatchorderId());
			om.setFkOrderId(order.getOrderId());
			orderMatchinfMapper.insert(om);
			
			return buyMatchOrderMapper.updateByPrimaryKey(buyMatchOrder);
		}
		@Override
		@Transactional(propagation = Propagation.REQUIRED) 
		public int clichSellMatchOrderById(SellMatchOrder sellMatchOrder,
				boolean isClinchOver,String payMethod) {
			SellGoods bg = sellGoodsMapper.selectByPrimaryKey(sellMatchOrder.getFkSellgoodsId());
			bg.setDsnum(bg.getDsnum().subtract(sellMatchOrder.getClinchNum()));//每次撮合都重新计算撮合成功后的需求数量
			if(isClinchOver){//所有需求数量撮合完成，把需求信息下架
				bg.setIsShelf(SystemConfig.INFO_OVER);//撮合成功后，需求信息下架
				
			} 
			sellGoodsMapper.updateByPrimaryKey(bg);//更新需求上下架状态及撮合后需求数量
			//生成待付款订单
			Order order = new Order();
			order.setBuyerUserName(sellMatchOrder.getBuyerUsersName());
			String currentTime = DataTimeUtil.getCurrentTime();
			order.setCreateTime(currentTime);
			order.setFkBuyerAddrId(sellMatchOrder.getFkBuyerAddrId());
			order.setFkBuyerUserId(sellMatchOrder.getFkBuyerUserId());
			order.setFkProductId(sellMatchOrder.getFkProductId());
			order.setFkSellerAddrId(sellMatchOrder.getFkSellerAddrId());
			order.setFkSellerUserId(sellMatchOrder.getFkSellerUserId());
			order.setMatchType(SystemConfig.MATCH_SELL);//获取撮合订单关联表时和orderid形成联合条件，可得到一个待撮合订单ID
			order.setMoneyTotal(sellMatchOrder.getClinchNum().multiply(sellMatchOrder.getClinchPrice()));
			order.setOperator(sellMatchOrder.getOperator());
			order.setOrderCode(SerialNumberUtil.getSerialCode(SystemConfig.NC));
			order.setOrderState(SystemConfig.ORDERSTATE_NOPAY);
			order.setOrderWeight(sellMatchOrder.getClinchNum());
			order.setSellerUserName(sellMatchOrder.getSellerUsersName());
			order.setSingleprice(sellMatchOrder.getClinchPrice());
			order.setPayMethod(payMethod);
			order.setFkSettleZhkbankId(2L);//默认建设银行，有时间改成后台选择框
			orderMapper.insertGetId(order);//生成待付款订单
			
			//生成待撮合订单与订单的关联信息
			OrderMatchinf om = new OrderMatchinf();
			om.setBuyOrSell(SystemConfig.MATCH_SELL);
			om.setFkMatchorderId(sellMatchOrder.getSellMatchorderId());
			om.setFkOrderId(order.getOrderId());
			orderMatchinfMapper.insert(om);
			
			return sellMatchOrderMapper.updateByPrimaryKey(sellMatchOrder);
		}
		@Override
		@Transactional(propagation = Propagation.REQUIRED)
		public int updateBuyMatchOrderById(MgrMatchOrderModel matchOrderModel ) {
			BuyMatchOrder bmo = getBuyMatchOrderById(matchOrderModel.getBuyMatchorderId());
			 //更新报价信息
			bmo.setOperator(matchOrderModel.getOperator()) ;//操作人
//			bmo.setSellerCompany(matchOrderModel.getSellerCompany());//公司名称暂时不能修改
			bmo.setOperatorRemark(matchOrderModel.getOperatorRemark() );
			bmo.setSellNum(matchOrderModel.getSellNum());
			bmo.setSellerRemark(matchOrderModel.getSellerRemark());
			bmo.setClinchPrice(matchOrderModel.getClinchPrice());
			bmo.setClinchStat(matchOrderModel.getClinchStat());
			bmo.setIsNeed(matchOrderModel.getIsNeed());
			bmo.setExpiredTime(matchOrderModel.getExpiredTime());
			bmo.setSellerQuota(matchOrderModel.getSellerQuota());
			bmo.setQuoteSingleprice(matchOrderModel.getQuoteSingleprice());
			 
			 //更新地址信息
			OrderAddress address = bmo.getSellerAddr();
			  AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getProvince()));
			  AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getCity()));
			  address.setProvince(province.getName());
			  address.setCity(city.getName());
			  address.setDetail(matchOrderModel.getDetail());
			  address.setLinkman(matchOrderModel.getLinkman());
			  address.setMobile(matchOrderModel.getMobile());
			  updateAddressById(address);
			return buyMatchOrderMapper.updateByPrimaryKey(bmo);
		}
		@Override
		@Transactional(propagation = Propagation.REQUIRED) 
		public int updateSellMatchOrderById(MgrMatchOrderModel matchOrderModel) {
			SellMatchOrder bmo = getSellMatchOrderById(matchOrderModel.getSellMatchorderId());
			 //更新报价信息
			bmo.setOperator(matchOrderModel.getOperator()) ;//操作人
//			bmo.setSellerCompany(matchOrderModel.getSellerCompany());//公司名称暂时不能修改
			bmo.setOperatorRemark(matchOrderModel.getOperatorRemark() );
			bmo.setBuyNum(matchOrderModel.getBuyNum());
			bmo.setBuyerRemark(matchOrderModel.getBuyerRemark());
			bmo.setClinchPrice(matchOrderModel.getClinchPrice());
			bmo.setClinchStat(matchOrderModel.getClinchStat());
			bmo.setIsNeed(matchOrderModel.getIsNeed());
			bmo.setBuyerQuota(matchOrderModel.getBuyerQuota());
			bmo.setExpiredTime(matchOrderModel.getExpiredTime());
			bmo.setQuoteSingleprice(matchOrderModel.getQuoteSingleprice());
			 
			 //更新地址信息
			OrderAddress address = bmo.getBuyerAddr();
			  AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getProvince()));
			  AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getCity()));
			  address.setProvince(province.getName());
			  address.setCity(city.getName());
			  address.setDetail(matchOrderModel.getDetail());
			  address.setLinkman(matchOrderModel.getLinkman());
			  address.setMobile(matchOrderModel.getMobile());
			  updateAddressById(address);
			return sellMatchOrderMapper.updateByPrimaryKey(bmo);
		}
		@Override
		@Transactional(propagation = Propagation.REQUIRED)
		public int updateSellMatchOrderById(SellMatchOrder sellMatchOrder) {
			return sellMatchOrderMapper.updateByPrimaryKey(sellMatchOrder);
		}

		@Override
		public SellMatchOrder getSellMatchOrderById(Long sellMatchorderId) {
			
			return sellMatchOrderMapper.getSellMatchOrderById(sellMatchorderId);
		}

		@Override
		public BuyMatchOrder getBuyMatchOrderById(Long buyMatchorderId) {
			return buyMatchOrderMapper.getBuyMatchOrderById(buyMatchorderId);
		}

		@Override
		public List<Map<String,String>> getAddressProvince() {
			// TODO Auto-generated method stub
			return addressDbMapper.getAddressProvince();
		}

		@Override
		public List<Map<String,String>> getAddressCity(Integer province) {
			// TODO Auto-generated method stub
			return addressDbMapper.getAddressCity(province);
		}

		@Override
		public int updateAddressById(OrderAddress address) {
			
			return orderAddressMapper.updateByPrimaryKey(address);
		}

		public OrderMapper getOrderMapper() {
			return orderMapper;
		}
		@Autowired
		public void setOrderMapper(OrderMapper orderMapper) {
			this.orderMapper = orderMapper;
		}

		public OrderMatchinfMapper getOrderMatchinfMapper() {
			return orderMatchinfMapper;
		}
		@Autowired
		public void setOrderMatchinfMapper(OrderMatchinfMapper orderMatchinfMapper) {
			this.orderMatchinfMapper = orderMatchinfMapper;
		}

		@Override
		public int insertBuyMatchOrder(MgrMatchOrderModel matchOrderModel) {
			
			//获取需求信息
			BuyGoods buyGoods = buygoodsServ.selectByFkId(matchOrderModel.getFkBuygoodsId() );
			
			BuyMatchOrder bmo = new BuyMatchOrder();
			 //更新报价信息
//			bmo.setOperator(matchOrderModel.getOperator()) ;//操作人---前台没有操作人
			bmo.setBuyMatchorderCode(SerialNumberUtil.getSerialCode(SystemConfig.IC));
//			bmo.setOperatorRemark(matchOrderModel.getOperatorRemark() );
			bmo.setSellNum(matchOrderModel.getSellNum());//销售数量
			bmo.setSellerRemark(matchOrderModel.getSellerRemark());//卖家备注
			bmo.setClinchStat(SystemConfig.MATCH_UN);//撮合状态-默认未撮合
			bmo.setIsNeed(SystemConfig.MATCH_BUY);//是否需要撮合
			bmo.setExpiredTime(SystemConfig.INFO_NOEXPIRED);//默认未过期
			bmo.setSellerQuota(matchOrderModel.getSellerQuota());//产品指标
			bmo.setQuoteSingleprice(matchOrderModel.getQuoteSingleprice());//卖家报价
			bmo.setBuyerUsersName(buyGoods.getBuyUser().getUsersName());
			
			bmo.setBuyinfoCode(buyGoods.getCode());//求购信息编号
			bmo.setCreateTime(DataTimeUtil.getCurrentTime());//建立时间
			bmo.setFkBuyerAddrId(buyGoods.getFkBuyerAddrId());//买家地址ID
			bmo.setFkBuyerUserId(buyGoods.getFkUsersId());//买家ID
			bmo.setFkBuygoodsId(matchOrderModel.getFkBuygoodsId());//需求信息ID
			bmo.setFkProductId(buyGoods.getFkProductId());//商品ID
			
			if(null != matchOrderModel.getFkSellerAddrId()){//如果卖家地址ID不为空，则是使用已有地址
				Address address =addressMapper.selectByPrimaryKey(matchOrderModel.getFkSellerAddrId());
				 //新增发布信息地址
			    OrderAddress orderAddress = new OrderAddress();
			    orderAddress.setFkUsersId(address.getFkUsersId());
			    orderAddress.setProvince(address.getProvince());
			    orderAddress.setCity(address.getCity());
			    orderAddress.setDetail(address.getDetail());
			    orderAddress.setMobile(address.getMobile());
			    orderAddress.setLinkman(address.getLinkman());
			    orderAddress.setCreateTime(DataTimeUtil.getCurrentTime());
			    orderAddressMapper.insertAddr(orderAddress); 
			bmo.setFkSellerAddrId(orderAddress.getAddressId());
			}else{//如果卖家地址为空，则新增一条卖家地址信息
				  AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getProvince()));
				  AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getCity()));
				//新增用户地址
				  Address address = new Address();
				  address.setFkUsersId(matchOrderModel.getFkSellerUserId());
				  address.setProvince(province.getName());
				  address.setCity(city.getName());
				  address.setDetail(matchOrderModel.getDetail());
				  address.setLinkman(matchOrderModel.getLinkman());
				  address.setMobile(matchOrderModel.getMobile());
				  addressMapper.insertAddr(address);
				//新增发布信息地址
				    OrderAddress orderAddress = new OrderAddress();
				    orderAddress.setFkUsersId(matchOrderModel.getFkSellerUserId());
				    orderAddress.setProvince(province.getName());
				    orderAddress.setCity(city.getName());
				    orderAddress.setDetail(matchOrderModel.getDetail());
				    orderAddress.setMobile(matchOrderModel.getMobile());
				    orderAddress.setLinkman(matchOrderModel.getLinkman());
				    orderAddress.setCreateTime(DataTimeUtil.getCurrentTime());
				    orderAddressMapper.insertAddr(orderAddress); 
				  
				  if("1".equals(matchOrderModel.getIsDefault())){
					  Users users = usersServ.selectByPrimaryKey(matchOrderModel.getFkSellerUserId());
						users.setDefAddressId(address.getAddressId());
						//修改默认地址
						usersServ.updateByPrimaryKeyWithBLOBs(users);
				  }
				  bmo.setFkSellerAddrId(orderAddress.getAddressId());  
			}
			
			bmo.setFkSellerUserId(matchOrderModel.getFkSellerUserId());//卖家ID
			bmo.setGoodsPic(matchOrderModel.getGoodsPic());//卖家上传的产品图片
			bmo.setLastModifyTime(DataTimeUtil.getCurrentTime());
			bmo.setSellerCompany(matchOrderModel.getSellerCompany());
			bmo.setSellerUsersName(matchOrderModel.getSellerUsersName());
			
			return buyMatchOrderMapper.insertfront(bmo);
		}

		@Override
		public int insertSellMatchOrder(MgrMatchOrderModel matchOrderModel) {
			
			//获取销售需求信息   这个地方  根据ID查询出来的就是null的 是，我之前也看到了。找sql
			SellGoods sellGoods = sellgoodsServ.selectSellGoodsById1( matchOrderModel.getFkSellgoodsId().toString() );
			
			
			SellMatchOrder bmo = new SellMatchOrder();
			 //更新询价信息
//			bmo.setOperator(matchOrderModel.getOperator()) ;//操作人
//			bmo.setSellerCompany(matchOrderModel.getSellerCompany());//公司名称暂时不能修改
			bmo.setSellMatchorderCode(SerialNumberUtil.getSerialCode(SystemConfig.EC));
			bmo.setOperatorRemark(matchOrderModel.getOperatorRemark() );
			bmo.setBuyNum(matchOrderModel.getBuyNum());
			bmo.setBuyerRemark(matchOrderModel.getBuyerRemark());
			bmo.setClinchStat(SystemConfig.MATCH_UN);//撮合状态-默认未撮合
			bmo.setIsNeed(SystemConfig.MATCH_BUY);//是否需要撮合
			bmo.setExpiredTime(SystemConfig.INFO_NOEXPIRED);//默认未过期
			bmo.setBuyerQuota(matchOrderModel.getBuyerQuota());//买方需求产品指标
			bmo.setQuoteSingleprice(matchOrderModel.getQuoteSingleprice());
			bmo.setSellerUsersName(sellGoods.getSellUser().getUsersName()); // sellGoods.getSellUser() 
			
			bmo.setSellinfoCode(sellGoods.getCode());//销售信息编号
			bmo.setCreateTime(DataTimeUtil.getCurrentTime());//建立时间
			bmo.setFkSellerAddrId(sellGoods.getFkSellerAddrId());//卖家地址ID
			bmo.setFkSellerUserId(sellGoods.getFkUsersId());//买家ID
			bmo.setFkSellgoodsId(matchOrderModel.getFkSellgoodsId());//销售信息ID
			bmo.setFkProductId(sellGoods.getFkProductId());//商品ID
			
			if(null != matchOrderModel.getFkBuyerAddrId()){//如果买家地址ID不为空，则是使用已有地址
				//新增发布信息地址
				Address address =addressMapper.selectByPrimaryKey(matchOrderModel.getFkBuyerAddrId());
			    OrderAddress orderAddress = new OrderAddress();
			    orderAddress.setFkUsersId(address.getFkUsersId());
			    orderAddress.setProvince(address.getProvince());
			    orderAddress.setCity(address.getCity());
			    orderAddress.setDetail(address.getDetail());
			    orderAddress.setMobile(address.getMobile());
			    orderAddress.setLinkman(address.getLinkman());
			    orderAddress.setCreateTime(DataTimeUtil.getCurrentTime());
			    orderAddressMapper.insertAddr(orderAddress); 
				bmo.setFkBuyerAddrId(orderAddress.getAddressId());
				}else{//如果卖家地址为空，则新增一条卖家地址信息
					 
					  AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getProvince()));
					  AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(matchOrderModel.getCity()));
					//新增用户地址
					  Address address = new Address();
					  address.setFkUsersId(matchOrderModel.getFkBuyerUserId());
					  address.setProvince(province.getName());
					  address.setCity(city.getName());
					  address.setDetail(matchOrderModel.getDetail());
					  address.setLinkman(matchOrderModel.getLinkman());
					  address.setMobile(matchOrderModel.getMobile());
					  addressMapper.insertAddr(address);
					  //新增发布信息地址
					    OrderAddress orderAddress = new OrderAddress();
					    orderAddress.setFkUsersId(matchOrderModel.getFkBuyerUserId());
					    orderAddress.setProvince(province.getName());
					    orderAddress.setCity(city.getName());
					    orderAddress.setDetail(matchOrderModel.getDetail());
					    orderAddress.setMobile(matchOrderModel.getMobile());
					    orderAddress.setLinkman(matchOrderModel.getLinkman());
					    orderAddress.setCreateTime(DataTimeUtil.getCurrentTime());
					    orderAddressMapper.insertAddr(orderAddress); 
					  if("1".equals(matchOrderModel.getIsDefault())){
						  Users users = usersServ.selectByPrimaryKey(matchOrderModel.getFkBuyerUserId());
							users.setDefAddressId(address.getAddressId());
							//修改公司默认地址
							usersServ.updateByPrimaryKeyWithBLOBs(users);
					  }
					  bmo.setFkBuyerAddrId(orderAddress.getAddressId());
				}
			bmo.setFkBuyerUserId(matchOrderModel.getFkBuyerUserId());//买家ID
			bmo.setGoodsPic(sellGoods.getDpic());//卖家上传的产品图片
			bmo.setLastModifyTime(DataTimeUtil.getCurrentTime());
			bmo.setBuyerCompany(matchOrderModel.getBuyerCompany());
			bmo.setBuyerUsersName(matchOrderModel.getBuyerUsersName());
			
			return sellMatchOrderMapper.insertfront(bmo);
		}

		public ISellGoodsServ getSellgoodsServ() {
			return sellgoodsServ;
		}
		@Autowired
		public void setSellgoodsServ(ISellGoodsServ sellgoodsServ) {
			this.sellgoodsServ = sellgoodsServ;
		}

		public IBuyGoodsServ getBuygoodsServ() {
			return buygoodsServ;
		}
		@Autowired
		public void setBuygoodsServ(IBuyGoodsServ buygoodsServ) {
			this.buygoodsServ = buygoodsServ;
		}

		public IUsersServ getUsersServ() {
			return usersServ;
		}
		@Autowired
		public void setUsersServ(IUsersServ usersServ) {
			this.usersServ = usersServ;
		}

		public OrderAddressMapper getOrderAddressMapper() {
			return orderAddressMapper;
		}
		@Autowired
		public void setOrderAddressMapper(OrderAddressMapper orderAddressMapper) {
			this.orderAddressMapper = orderAddressMapper;
		}

	

		

}
