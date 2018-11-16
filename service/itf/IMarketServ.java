package com.shop.service.itf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.shop.dao.entity.Market;
import com.shop.dao.entity.MarketValue;

public interface IMarketServ {
	int deleteByPrimaryKey(Long breedId);
    int insert(Market record);
    int insertSelective(Market record);
    Market selectByPrimaryKey(Long breedId);
    int updateByPrimaryKeySelective(Market record);
    int updateByPrimaryKey(Market record);
    List<Market> getMarket(Market market);
	int getMarketMsgCount();
	List<Market> getMarketByStatus();
	
	//添加商品曲线值
	int insertMarketValue(MarketValue record);
	/**曲线图相关*/
	Market getMarketById(Long breedId);
	String makeLine(Market market,int width ,int height, HttpServletRequest request) ;
}
