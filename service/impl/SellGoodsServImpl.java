package com.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shop.common.utils.CommonDatas;
import com.shop.common.utils.DataTimeUtil;
import com.shop.common.utils.SerialNumberUtil;
import com.shop.common.utils.SystemConfig;
import com.shop.controller.model.MgrGoodsModel;
import com.shop.dao.entity.Address;
import com.shop.dao.entity.AddressDb;
import com.shop.dao.entity.OrderAddress;
import com.shop.dao.entity.SellGoods;
import com.shop.dao.entity.Users;
import com.shop.dao.itf.AddressDbMapper;
import com.shop.dao.itf.AddressMapper;
import com.shop.dao.itf.OrderAddressMapper;
import com.shop.dao.itf.ProductMapper;
import com.shop.dao.itf.SellGoodsMapper;
import com.shop.dao.itf.UsersMapper;
import com.shop.service.itf.ISellGoodsServ;

@Service("SellGoodsServImpl")
public class SellGoodsServImpl implements ISellGoodsServ{
	private SellGoodsMapper sellGoodsMapper;
	private UsersMapper usersMapper;
	private ProductMapper productMapper;
	private AddressDbMapper addressDbMapper;
	private AddressMapper addressMapper;
	private OrderAddressMapper orderAddressMapper;
	public UsersMapper getUsersMapper() {
		return usersMapper;
	}
	@Autowired
	public void setUsersMapper(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}

	public ProductMapper getProductMapper() {
		return productMapper;
	}
	@Autowired
	public void setProductMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	public AddressDbMapper getAddressDbMapper() {
		return addressDbMapper;
	}
	@Autowired
	public void setAddressDbMapper(AddressDbMapper addressDbMapper) {
		this.addressDbMapper = addressDbMapper;
	}

	public AddressMapper getAddressMapper() {
		return addressMapper;
	}
	@Autowired
	public void setAddressMapper(AddressMapper addressMapper) {
		this.addressMapper = addressMapper;
	}

	@Override
	public int getAllSellGoodsCount() {
		return sellGoodsMapper.getAllSellGoodsCount();
	}

	@Override
	public List<SellGoods> selectAllSellGoodsList(SellGoods sgoods, String param) {
		sgoods.computeStart();
		return sellGoodsMapper.selectAllSellGoodsList(sgoods, param);
	}

	@Override
	public SellGoods getCurrentSellGoods(Long sellGoodsId) {
		return sellGoodsMapper.getCurrentSellGoods(sellGoodsId);
	}

	@Override
	public int updateSellGoods(SellGoods sgoods) {
		return sellGoodsMapper.updateSellGoods(sgoods);
	}

	public SellGoodsMapper getSellGoodsMapper() {
		return sellGoodsMapper;
	}
	@Autowired
	public void setSellGoodsMapper(SellGoodsMapper sellGoodsMapper) {
		this.sellGoodsMapper = sellGoodsMapper;
	}

	@Override
	public SellGoods getSellGoodsById(Long sellGoodsId) {
		return sellGoodsMapper.getSellGoodsById(sellGoodsId);
	}

	@Override
	public int updateByPrimaryKey(SellGoods sgs) {
		return sellGoodsMapper.updateByPrimaryKey(sgs);
	}

	@Override
	public int deleteSellGoodsById(Long sellGoodsId) {
		return sellGoodsMapper.deleteSellGoodsById(sellGoodsId);
	}

	@Override
	public int updateSellGoodsById(SellGoods sgs) {
		return sellGoodsMapper.updateSellGoodsById(sgs);
	}

	@Override
	public int updateById(SellGoods sgs) {
		return sellGoodsMapper.updateById(sgs);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(SellGoods sellGoods) {
		return sellGoodsMapper.updateByPrimaryKeyWithBLOBs(sellGoods);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateSellGoodById(MgrGoodsModel sellGoodsModel) {
		SellGoods sgs = getSellGoodsById(sellGoodsModel.getSellGoodsId());
		sgs.setOperator(sellGoodsModel.getOperator());
		sgs.setDsyremark(sellGoodsModel.getDsyremark());
		//bgs.setDeadline(buyGoodsModel.getDeadline());
		sgs.setDsnum(sellGoodsModel.getDsnum());
		sgs.setDsprice(sellGoodsModel.getDsprice());
		sgs.setDstarget(sellGoodsModel.getDstarget());
		sgs.setDsremark(sellGoodsModel.getDsremark());
		//bgs.setDpic(buyGoodsModel.getDpic()); 
		//更新用户信息
		Users users = sgs.getSellUser();
		users.setUsersName(sellGoodsModel.getUsersName());
		users.setCompany(sellGoodsModel.getCompany());
		users.setMobile(sellGoodsModel.getMobile());
		updateUsersById(users);
		//更新地址信息
		OrderAddress address = sgs.getSellerAddr();
	  AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(sellGoodsModel.getProvince()));
	  AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(sellGoodsModel.getCity()));
	  address.setProvince(province.getName());
	  address.setCity(city.getName());
	  address.setDetail(sellGoodsModel.getDetail());
	  address.setLinkman(sellGoodsModel.getLinkman());
	  address.setMobile(sellGoodsModel.getMobile());
	  address.setCreateTime(DataTimeUtil.getCurrentTime());
	  updateAddressById(address); 
		
		return sellGoodsMapper.updateByPrimaryKey(sgs);
	}
	@Override
	public int updateAddressById(OrderAddress address) {
		
		return orderAddressMapper.updateByPrimaryKey(address);
	}
	@Override
	public List<Map<String, String>> getAddressProvince() {
		return addressDbMapper.getAddressProvince();
	}
	@Override
	public List<Map<String,String>> getAddressCity(Integer province) {
		return addressDbMapper.getAddressCity(province);
	}
	@Override
	public int updateUsersById(Users users) {
		return usersMapper.updateByPrimaryKey(users);
	}
	@Override
	public int insert(SellGoods sellGoods) {
		return sellGoodsMapper.insert(sellGoods);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int insert(MgrGoodsModel sellGoodsModel) {
		SellGoods sgs = new SellGoods();
		sgs.setCode(SerialNumberUtil.getSerialCode(SystemConfig.SC));
		sgs.setDsynum(sellGoodsModel.getDsnum());//原始发布吨数
		sgs.setDpic(sellGoodsModel.getDpic());
		sgs.setDnumber(sellGoodsModel.getDnumber());
		sgs.setDstime(DataTimeUtil.getCurrentTime());
		sgs.setDstat(SystemConfig.INFO_NOAUTH);
		sgs.setDeadline(sellGoodsModel.getDeadline());
		sgs.setFkUsersId(sellGoodsModel.getFkUsersId());
		sgs.setIsShelf(SystemConfig.INFO_DOWNSHELF);
		sgs.setOperator(SecurityUtils.getSubject()
				.getPrincipal().toString());
		sgs.setLastModifyTime(sellGoodsModel.getLastModifyTime());
		sgs.setExpired(sellGoodsModel.getExpired());
		sgs.setDscompany(sellGoodsModel.getDscompany());
		
		sgs.setDsyremark(sellGoodsModel.getDsyremark());
		sgs.setDsnum(sellGoodsModel.getDsnum());
		sgs.setDsprice(sellGoodsModel.getDsprice());
		sgs.setDstarget(sellGoodsModel.getDstarget());
		sgs.setDsremark(sellGoodsModel.getDsremark());
		
		sgs.setFkProductId(sellGoodsModel.getFkProductId());
		
		//Users users = bgs.getBuyUser();
		//bgs.setFkUsersId(users.getUsersId());
		//users.setMobile(users.getMobile());
		//usersMapper.insert(users);
		
		OrderAddress address = new OrderAddress();
	    AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(sellGoodsModel.getProvince()));
	    AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(sellGoodsModel.getCity()));
	    address.setFkUsersId(sellGoodsModel.getFkUsersId());
	    address.setProvince(province.getName());
	    address.setCity(city.getName());
 	    address.setLinkman(sellGoodsModel.getLinkman());
	    address.setDetail(sellGoodsModel.getDetail());
	    address.setMobile(sellGoodsModel.getMobile());
	    address.setCreateTime(DataTimeUtil.getCurrentTime());
	    orderAddressMapper.insertAddr(address); 
	    sgs.setFkSellerAddrId(address.getAddressId());
		return sellGoodsMapper.insert(sgs);
	}
	@Override
	public List<SellGoods> selectSellGoodsListForIndex(SellGoods sellGoods,
			Byte dstat, String productName) {
		sellGoods.setRows(CommonDatas.sellGoodsNum);
		return sellGoodsMapper.selectSellGoodsListForIndex(sellGoods,dstat,productName);
	}
	@Override
	public List<SellGoods> selectSellGoodsList(SellGoods sellGoods, Byte dstat,
			String productName) {
		return sellGoodsMapper.selectSellGoodsListForIndex(sellGoods,dstat,productName);
	}
	@Override
	public SellGoods selectSellGoods(String sellGoodsId) {
		return sellGoodsMapper.selectSellGoodsById(Long.parseLong(sellGoodsId));  
	}
	@Override
	public SellGoods selectSellGoodsById1(String sellGoodsId) {
		return sellGoodsMapper.selectSellGoodsById1(Long.parseLong(sellGoodsId));
	}
	@Override
	public List<SellGoods> selectAllSellGoods(int min, int max) {
		return sellGoodsMapper.selectAllSellGoods(min,max);
	}
	@Override
	public List<SellGoods> searchList(String para,int min,int max) {
		
		return sellGoodsMapper.searchList(para,  min,  max);
	}
	@Override
	public int getsearchListCount(String para, int min, int max) {
		// TODO Auto-generated method stub
		return sellGoodsMapper.getsearchListCount(para,  min,  max);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertFront(MgrGoodsModel sellGoodsModel) {
		SellGoods sgs = new SellGoods();
		sgs.setCode(SerialNumberUtil.getSerialCode(SystemConfig.SC));
		sgs.setDsynum(sellGoodsModel.getDsnum());
		sgs.setDpic(sellGoodsModel.getDpic());
		sgs.setDnumber(sellGoodsModel.getDnumber());
		sgs.setDstime(DataTimeUtil.getCurrentTime());
		sgs.setDstat(SystemConfig.INFO_NOAUTH);
		sgs.setDeadline(sellGoodsModel.getDeadline());
		sgs.setFkUsersId(sellGoodsModel.getFkUsersId());
		sgs.setIsShelf(SystemConfig.INFO_DOWNSHELF);
		sgs.setLastModifyTime(DataTimeUtil.getCurrentTime());
		sgs.setExpired("0");
//		sgs.setDscompany(sellGoodsModel.getDscompany());
		
		sgs.setDsyremark(sellGoodsModel.getDsyremark());
		sgs.setDsnum(sellGoodsModel.getDsnum());
		sgs.setDsprice(sellGoodsModel.getDsprice());
		sgs.setDstarget(sellGoodsModel.getDstarget());
		sgs.setDsremark(sellGoodsModel.getDsremark());
		
		sgs.setFkProductId( sellGoodsModel.getFkProductId() );
		
		
		if(null != sellGoodsModel.getFkSellerAddrId()){//如果卖家地址ID不为空，则是使用已有地址
			Address address =addressMapper.selectByPrimaryKey(sellGoodsModel.getFkSellerAddrId());
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
			sgs.setFkSellerAddrId(orderAddress.getAddressId());
			}else{//如果卖家地址为空，则新增一条卖家地址信息
		//新增用户地址
		Address address = new Address();
	    AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(sellGoodsModel.getProvince()));
	    AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(sellGoodsModel.getCity()));
	    address.setFkUsersId(sellGoodsModel.getFkUsersId());
	    address.setProvince(province.getName());
	    address.setCity(city.getName());
	    address.setLinkman(sellGoodsModel.getLinkman());
	    address.setDetail(sellGoodsModel.getDetail());
	    address.setMobile(sellGoodsModel.getMobile());
	    address.setCreateTime(DataTimeUtil.getCurrentTime());
	    addressMapper.insertAddr(address); 
	  //新增发布信息地址
	    OrderAddress orderAddress = new OrderAddress();
	    orderAddress.setFkUsersId(sellGoodsModel.getFkUsersId());
	    orderAddress.setProvince(province.getName());
	    orderAddress.setCity(city.getName());
	    orderAddress.setDetail(sellGoodsModel.getDetail());
	    orderAddress.setMobile(sellGoodsModel.getMobile());
	    orderAddress.setLinkman(sellGoodsModel.getLinkman());
	    orderAddress.setCreateTime(DataTimeUtil.getCurrentTime());
	    orderAddressMapper.insertAddr(orderAddress); 
	    if("1".equals(sellGoodsModel.getIsDefault())){
			  Users users = usersMapper.selectByPrimaryKey(sellGoodsModel.getFkUsersId());
				users.setDefAddressId(address.getAddressId());
				//修改默认地址
				usersMapper.updateByPrimaryKey(users);
		  }
	    sgs.setFkSellerAddrId(orderAddress.getAddressId());
			}
	    
		return sellGoodsMapper.insert(sgs);
	}
	@Override
	public int selectSellGoodsListCount(SellGoods sellGoods, Byte dstat,
			String productName) {
		// TODO Auto-generated method stub
		return sellGoodsMapper.selectSellGoodsListForIndexCount(sellGoods,dstat,productName);
	}
	@Override
	public List<SellGoods> selectSellGoodsByCondition(Map<String, Object> params) {
		return  sellGoodsMapper.selectSellGoodsByCondition(params);
	}
	@Override
	public int getGoodsByConditionCount(Map<String, Object> params) {
		return sellGoodsMapper.getGoodsByConditionCount(params);
	}
	@Override
	public List<SellGoods> selectMySellsList(Long usersId, int min, int max,
			String dstat) {
		return sellGoodsMapper.selectMySellsList(usersId,min,max,dstat);
	}
	@Override
	public int countSelectMySellsList(Long usersId, String dstat) {
		return sellGoodsMapper.countSelectMySellsList(usersId,dstat);
	}
	public OrderAddressMapper getOrderAddressMapper() {
		return orderAddressMapper;
	}
	@Autowired
	public void setOrderAddressMapper(OrderAddressMapper orderAddressMapper) {
		this.orderAddressMapper = orderAddressMapper;
	}

}
