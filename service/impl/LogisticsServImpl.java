package com.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.Logistics;
import com.shop.dao.entity.LogisticsInfo;
import com.shop.dao.itf.LogisticsInfoMapper;
import com.shop.dao.itf.LogisticsMapper;
import com.shop.service.itf.ILogisticsServ;
@Service("LogisticsServImpl")
public class LogisticsServImpl implements ILogisticsServ {
	private LogisticsMapper logisticsMapper;
	private LogisticsInfoMapper logisticsInfoMapper;
	public LogisticsMapper getLogisticsMapper() {
		return logisticsMapper;
	}
	@Autowired
	public void setLogisticsMapper(LogisticsMapper logisticsMapper) {
		this.logisticsMapper = logisticsMapper;
	}
	public LogisticsInfoMapper getLogisticsInfoMapper() {
		return logisticsInfoMapper;
	}
	@Autowired
	public void setLogisticsInfoMapper(LogisticsInfoMapper logisticsInfoMapper) {
		this.logisticsInfoMapper = logisticsInfoMapper;
	}
	@Override
	public List<Logistics> selectLogisticsList(Logistics logistics,String param) {
		// TODO Auto-generated method stub
		return logisticsMapper.selectLogisticsList(logistics, param);
	}
	@Override
	public int getLogisticsCount(Logistics logistics,String param) {
		// TODO Auto-generated method stub
		return logisticsMapper.getLogisticsCount(logistics, param);
	}
	@Override
	public List<LogisticsInfo> selectLogisticsInfoList(
			LogisticsInfo logisticsInfo,String param) {
		// TODO Auto-generated method stub
		return logisticsInfoMapper.selectLogisticsInfoList(logisticsInfo, param);
	}
	@Override
	public int getLogisticsInfoCount(LogisticsInfo logisticsInfo,String param) {
		// TODO Auto-generated method stub
		return logisticsInfoMapper.getLogisticsInfoCount(logisticsInfo, param);
	}
	@Override
	public Logistics selectLogisticsById(Long logisticsId) {
		return logisticsMapper.selectByPrimaryKey(logisticsId);
	}
	@Override
	public LogisticsInfo selectLogisticsInfoById(Long logisticsInfoId) {
		return logisticsInfoMapper.selectByPrimaryKey(logisticsInfoId);
	}
	@Override
	public int insertLogistics(Logistics logistics) {
		return logisticsMapper.insert(logistics);
	}
	@Override
	public int updateLogistics(Logistics logistics) {
		return logisticsMapper.updateByPrimaryKey(logistics);
	}
}
