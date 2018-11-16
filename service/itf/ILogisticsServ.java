package com.shop.service.itf;

import java.util.List;

import com.shop.dao.entity.Logistics;
import com.shop.dao.entity.LogisticsInfo;
import com.shop.dao.entity.OrderDelivery;

public interface ILogisticsServ {
	
	int insertLogistics(Logistics logistics);
	int updateLogistics(Logistics logistics);
	/**
	  * 方法描述：获取所有物流公司
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:05:47
	  */
	List<Logistics> selectLogisticsList(Logistics logistics,String param);
	int getLogisticsCount(Logistics logistics,String param);
	
	/**
	  * 方法描述：获取所有物流信息
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: song
	  * @version: 2015-11-19 下午2:05:47
	  */
	List<LogisticsInfo> selectLogisticsInfoList(LogisticsInfo logisticsInfo,String param);
	int getLogisticsInfoCount(LogisticsInfo logisticsInfo,String param);
	
	Logistics selectLogisticsById(Long id); 
	LogisticsInfo selectLogisticsInfoById(Long id); 
}
