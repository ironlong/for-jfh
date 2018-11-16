package com.shop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.Articl;
import com.shop.dao.itf.ArticlMapper;
import com.shop.service.itf.IArticlServ;

@Service("ArticlServImpl")
public class ArticlServImpl implements IArticlServ{
	private ArticlMapper articlMapper;

	public ArticlMapper getArticlMapper() {
		return articlMapper;
	}
	@Autowired
	public void setArticlMapper(ArticlMapper articlMapper) {
		this.articlMapper = articlMapper;
	}

	@Override
	public List<Articl> getArticls(Articl articls) {
		// TODO Auto-generated method stub
		articls.computeStart();
		return articlMapper.getArticls(articls.getStart(),articls.getRows());
	}

	@Override
	public int getArticlsCount() {
		// TODO Auto-generated method stub
		return articlMapper.getArticlsCount();
	}

	@Override
	public Articl getArticlsByArticlName(String articlsName) {
		// TODO Auto-generated method stub
		return articlMapper.getArticlsByArticlName(articlsName);
	}

	@Override
	public Articl selectByPrimaryKey(long parseLong) {
		// TODO Auto-generated method stub
		return articlMapper.selectByPrimaryKey(parseLong);
	}

	@Override
	public List<Articl> getArticlsByInput(String str) {
		// TODO Auto-generated method stub
		return articlMapper.getArticlsByInput(str);
	}

	@Override
	public int deleteByIds(long parseLong) {
		// TODO Auto-generated method stub
		return articlMapper.deleteByPrimaryKey(parseLong);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Articl articls) {
		// TODO Auto-generated method stub
		return articlMapper.updateByPrimaryKeyWithBLOBs(articls);
	}

	@Override
	public int insertArticls(Articl articls) {
		// TODO Auto-generated method stub
		return articlMapper.insert(articls);
	}
	@Override
	public List<Articl> getArticlList(Articl articl) {
		// TODO Auto-generated method stub
		return articlMapper.getArticlList(articl);
	}
	
}
