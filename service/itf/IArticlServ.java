package com.shop.service.itf;

import java.util.List;

import com.shop.dao.entity.Articl;

public interface IArticlServ {

	List<Articl> getArticls(Articl articls);

	int getArticlsCount();

	Articl getArticlsByArticlName(String articlsName);

	Articl selectByPrimaryKey(long parseLong);

	List<Articl> getArticlsByInput(String str);

	int deleteByIds(long parseLong);

	int updateByPrimaryKeyWithBLOBs(Articl articls);

	int insertArticls(Articl articls);

	List<Articl> getArticlList(Articl articl);

}
