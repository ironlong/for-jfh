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
import com.shop.dao.entity.BuyGoods;
import com.shop.dao.entity.OrderAddress;
import com.shop.dao.entity.Users;
import com.shop.dao.itf.AddressDbMapper;
import com.shop.dao.itf.AddressMapper;
import com.shop.dao.itf.BuyGoodsMapper;
import com.shop.dao.itf.OrderAddressMapper;
import com.shop.dao.itf.ProductMapper;
import com.shop.dao.itf.UsersMapper;
import com.shop.service.itf.IBuyGoodsServ;
@Service("BuyGoodsServImpl")
public class BuyGoodsServImpl implements IBuyGoodsServ{
	BuyGoodsMapper buyGoodsMapper;
	UsersMapper usersMapper;
	ProductMapper productMapper;
	AddressDbMapper addressDbMapper;
	AddressMapper addressMapper;
	OrderAddressMapper orderAddressMapper;
	
	public AddressDbMapper getAddressDbMapper() {
		return addressDbMapper;
	}
	@Autowired
	public void setAddressDbMapper(AddressDbMapper addressDbMapper) {
		this.addressDbMapper = addressDbMapper;
	}
	
	public BuyGoodsMapper getBuyGoodsMapper() {
		return buyGoodsMapper;
	}
	@Autowired
	public void setBuyGoodsMapper(BuyGoodsMapper buyGoodsMapper) {
		this.buyGoodsMapper = buyGoodsMapper;
	}
	
	@Override
	public int getAllBuyGoodsCount() {
		return buyGoodsMapper.getAllBuyGoodsCount();
	}
	
	public UsersMapper getUsersMapper() {
		return usersMapper;
	}
	@Autowired
	public void setUsersMapper(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}	
	

	@Override
	public BuyGoods getCurrentBuyGoods() {
		return buyGoodsMapper.getCurrentBuyGoods();
	}

	@Override
	public int updateBuyGoods(BuyGoods bgoods) {
		return buyGoodsMapper.updateBuyGoods(bgoods);
	}

	@Override
	public List<BuyGoods> selectAllBuyGoodsList(BuyGoods bgoods, String param) {
		bgoods.computeStart();
		return buyGoodsMapper.selectAllBuyGoodsList(bgoods,param);
	}
	@Override
	public BuyGoods getBuyGoodsById(Long buyGoodId) {
		return buyGoodsMapper.getBuyGoodsById(buyGoodId);
	}
	
	@Override
	public int deleteBuyGoodsById(Long buyGoodId) {
		return buyGoodsMapper.deleteBuyGoodsById(buyGoodId);
	}
	@Override
	public int updateBuyGoodsById(BuyGoods buyGoods) {
		return buyGoodsMapper.updateBuyGoodsById(buyGoods);
	}
	@Override
	public BuyGoods selectByPrimaryKey(Long buyGoodId) {
		return buyGoodsMapper.selectByPrimaryKey(buyGoodId);
	}
	@Override
	public int updateByPrimaryKeyWithBLOBs(BuyGoods buyGoods) {
		return buyGoodsMapper.updateByPrimaryKeyWithBLOBs(buyGoods);
	}
	@Override
	public int updateByPrimaryKey(BuyGoods bgs) {
		return buyGoodsMapper.updateByPrimaryKey(bgs);
	}
	@Override
	public int updateById(BuyGoods bgs) {
		return buyGoodsMapper.updateById(bgs);
	}
	@Override
	public int insert(BuyGoods buyGoods) {
		return buyGoodsMapper.insert(buyGoods);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateBuyGoodById(MgrGoodsModel buyGoodsModel) {
		
		BuyGoods bgs = getBuyGoodsById(buyGoodsModel.getBuyGoodId());
		bgs.setOperator(buyGoodsModel.getOperator());
		bgs.setDbremarker(buyGoodsModel.getDbremarker());
		bgs.setDbnum(buyGoodsModel.getDbnum());
		bgs.setDbprice(buyGoodsModel.getDbprice());
		bgs.setDbtarget(buyGoodsModel.getDbtarget());
		bgs.setDbremark(buyGoodsModel.getDbremark());
		//更新用户信息
		Users users = bgs.getBuyUser();
		users.setUsersName(buyGoodsModel.getUsersName());
		users.setCompany(buyGoodsModel.getCompany());
		users.setMobile(buyGoodsModel.getMobile());
		updateUsersById(users);
		
		//更新地址信息
	   OrderAddress address = bgs.getBuyerAddr();
	  AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(buyGoodsModel.getProvince()));
	  AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(buyGoodsModel.getCity()));
	  address.setProvince(province.getName());
	  address.setCity(city.getName());
	  address.setDetail(buyGoodsModel.getDetail());
	  address.setLinkman(buyGoodsModel.getLinkman());
	  address.setMobile(buyGoodsModel.getMobile());
	  address.setCreateTime(DataTimeUtil.getCurrentTime());
	  updateAddressById(address); 
		
		return buyGoodsMapper.updateByPrimaryKey(bgs);
	}
	public AddressMapper getAddressMapper() {
		return addressMapper;
	}
	@Autowired
	public void setAddressMapper(AddressMapper addressMapper) {
		this.addressMapper = addressMapper;
	}
	@Override
	public BuyGoods selectByFkId(long parseLong) {
		return buyGoodsMapper.selectByFkId(parseLong);
	}
	public ProductMapper getProductMapper() {
		return productMapper;
	}
	@Autowired
	public void setProductMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
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
	public int updateAddressById(OrderAddress address) {
		
		return orderAddressMapper.updateByPrimaryKey(address);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int insert(MgrGoodsModel buyGoodsModel) {
		BuyGoods bgs = new BuyGoods();
		bgs.setCode(SerialNumberUtil.getSerialCode(SystemConfig.BC));
		bgs.setDsynum(buyGoodsModel.getDbnum());//原始需求总量
		bgs.setDpic(buyGoodsModel.getDpic());
		bgs.setDnumber(buyGoodsModel.getDnumber());
		bgs.setDbtime(DataTimeUtil.getCurrentTime());
		bgs.setDstat(SystemConfig.INFO_NOAUTH);
		bgs.setDeadline(buyGoodsModel.getDeadline());
		bgs.setFkUsersId(buyGoodsModel.getFkUsersId());
		bgs.setIsShelf(SystemConfig.INFO_DOWNSHELF);
		bgs.setOperator(SecurityUtils.getSubject()
				.getPrincipal().toString());
		bgs.setLastModifyTime(buyGoodsModel.getLastModifyTime());
		bgs.setExpired(buyGoodsModel.getExpired());
		bgs.setDbcompany(buyGoodsModel.getDbcompany());
		
		bgs.setDbremarker(buyGoodsModel.getDbremarker());
		bgs.setDbnum(buyGoodsModel.getDbnum());
		bgs.setDbprice(buyGoodsModel.getDbprice());
		bgs.setDbtarget(buyGoodsModel.getDbtarget());
		bgs.setDbremark(buyGoodsModel.getDbremark());
		bgs.setFkProductId(buyGoodsModel.getFkProductId());
	 
		//Users users = bgs.getBuyUser();
		//bgs.setFkUsersId(users.getUsersId());
		//users.setMobile(users.getMobile());
		//usersMapper.insert(users);
		
		OrderAddress address = new OrderAddress();
	    AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(buyGoodsModel.getProvince()));
	    AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(buyGoodsModel.getCity()));
	    address.setFkUsersId(buyGoodsModel.getFkUsersId());
	    address.setProvince(province.getName());
	    address.setCity(city.getName());
//	    bgs.setFkBuyerAddrId(buyGoodsModel.getFkBuyerAddrId());
	    address.setDetail(buyGoodsModel.getDetail());
	    address.setMobile(buyGoodsModel.getMobile());
	    address.setLinkman(buyGoodsModel.getLinkman());
	    address.setCreateTime(DataTimeUtil.getCurrentTime());
	    orderAddressMapper.insertAddr(address); 
	    bgs.setFkBuyerAddrId(address.getAddressId());
		return buyGoodsMapper.insert(bgs);
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertFront(MgrGoodsModel buyGoodsModel) {
		BuyGoods bgs = new BuyGoods();
		bgs.setCode(SerialNumberUtil.getSerialCode(SystemConfig.BC));
		bgs.setDsynum(buyGoodsModel.getDbnum());//原始需求总量
		bgs.setDpic(buyGoodsModel.getDpic());
		bgs.setDnumber(buyGoodsModel.getDnumber());
		bgs.setDbtime(DataTimeUtil.getCurrentTime());
		bgs.setDstat(SystemConfig.INFO_NOAUTH);
		bgs.setDeadline(buyGoodsModel.getDeadline());
		bgs.setFkUsersId(buyGoodsModel.getFkUsersId());
		bgs.setIsShelf(SystemConfig.INFO_DOWNSHELF);
		bgs.setLastModifyTime(DataTimeUtil.getCurrentTime());
		bgs.setExpired("0");
		bgs.setDbcompany(buyGoodsModel.getDbcompany());
		
		bgs.setDbremarker(buyGoodsModel.getDbremarker());
		bgs.setDbnum(buyGoodsModel.getDbnum());
		bgs.setDbprice(buyGoodsModel.getDbprice());
		bgs.setDbtarget(buyGoodsModel.getDbtarget());
		bgs.setDbremark(buyGoodsModel.getDbremark());
		bgs.setFkProductId(buyGoodsModel.getFkProductId());
	 
		//Users users = bgs.getBuyUser();
		//bgs.setFkUsersId(users.getUsersId());
		//users.setMobile(users.getMobile());
		//usersMapper.insert(users);
		if(null != buyGoodsModel.getFkBuyerAddrId()){//如果卖家地址ID不为空，则是使用已有地址
			Address address =addressMapper.selectByPrimaryKey(buyGoodsModel.getFkBuyerAddrId());
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
			 bgs.setFkBuyerAddrId(orderAddress.getAddressId());
			}else{//如果卖家地址为空，则新增一条卖家地址信息
				
			    AddressDb province = addressDbMapper.selectByPrimaryKey(Integer.parseInt(buyGoodsModel.getProvince()));
			    AddressDb city = addressDbMapper.selectByPrimaryKey(Integer.parseInt(buyGoodsModel.getCity()));
			    //新增用户地址
			    Address address = new Address();
			    address.setFkUsersId(buyGoodsModel.getFkUsersId());
			    address.setProvince(province.getName());
			    address.setCity(city.getName());
			    address.setDetail(buyGoodsModel.getDetail());
			    address.setMobile(buyGoodsModel.getMobile());
			    address.setLinkman(buyGoodsModel.getLinkman());
			    address.setCreateTime(DataTimeUtil.getCurrentTime());
			    addressMapper.insertAddr(address); 
			    //新增发布信息地址
			    OrderAddress orderAddress = new OrderAddress();
			    orderAddress.setFkUsersId(buyGoodsModel.getFkUsersId());
			    orderAddress.setProvince(province.getName());
			    orderAddress.setCity(city.getName());
			    orderAddress.setDetail(buyGoodsModel.getDetail());
			    orderAddress.setMobile(buyGoodsModel.getMobile());
			    orderAddress.setLinkman(buyGoodsModel.getLinkman());
			    orderAddress.setCreateTime(DataTimeUtil.getCurrentTime());
			    orderAddressMapper.insertAddr(orderAddress); 
			    if("1".equals(buyGoodsModel.getIsDefault())){
					  Users users = usersMapper.selectByPrimaryKey(buyGoodsModel.getFkUsersId());
						users.setDefAddressId(address.getAddressId());
						//修改默认地址
						usersMapper.updateByPrimaryKey(users);
				  }
			    bgs.setFkBuyerAddrId(orderAddress.getAddressId());
			}
		return buyGoodsMapper.insert(bgs);
	}
	@Override
	public int updateUsersById(Users users) {
		return usersMapper.updateByPrimaryKey(users);
	}
	@Override
	public List<BuyGoods> selectBuyGoodsListForIndex(BuyGoods buyGoods,byte dstat) {
		buyGoods.setRows(CommonDatas.buyGoodsNum);
		return buyGoodsMapper.selectBuyGoodsListForIndex(buyGoods,dstat);
	}
	@Override
	public List<BuyGoods> selectBuyGoodsListByName(BuyGoods buyGoods,byte dstat,String productName) {
		
		return buyGoodsMapper.selectBuyGoodsListByName(buyGoods,dstat,productName);
	}
	@Override
	public List<BuyGoods> selectAllBuyGoods(int min,int max) {
		return buyGoodsMapper.selectAllBuyGoods(min,max);
	}
	@Override
	public BuyGoods selectBuyGoodsById(Long buyGoodId) {
		// TODO Auto-generated method stub
		return buyGoodsMapper.selectBuyGoodsById(buyGoodId);
	}
	@Override
	public List<BuyGoods> searchList(String para,int min,int max) {
		
		return buyGoodsMapper.searchList(para,  min,  max);
	}
	@Override
	public int getsearchListCount(String para, int min, int max) {
		// TODO Auto-generated method stub
		return buyGoodsMapper.getsearchListCount(para,  min,  max);
	}
	@Override
	public List<BuyGoods> selectBuyGoodsByCondition(Map<String, Object> params) {
		return buyGoodsMapper.selectBuyGoodsByCondition(params);
	}
	@Override
	public int getGoodsByConditionCount(Map<String, Object> params) {
		return buyGoodsMapper.getGoodsByConditionCount(params);
	}
	@Override
	public List<BuyGoods> selectMyBuysList(Long usersId, int min, int max,
			String dstat) {
		return buyGoodsMapper.selectMyBuysList(usersId,min,max,dstat);
	}
	@Override
	public int countSelectMyBuysList(Long usersId, String dstat) {
		return buyGoodsMapper.countSelectMyBuysList(usersId,dstat);
	}
	public OrderAddressMapper getOrderAddressMapper() {
		return orderAddressMapper;
	}
	@Autowired
	public void setOrderAddressMapper(OrderAddressMapper orderAddressMapper) {
		this.orderAddressMapper = orderAddressMapper;
	}
}
