package com.shop.service.itf;

import java.util.List;
import java.util.Map;

import com.shop.dao.entity.Product;

public interface IProductServ {
//	int deleteByPrimaryKey(String id);
//	int insert(Product record);
//	int insertSelective(Product record);
//	Product selectByPrimaryKey(String id);
//	int updateByPrimaryKeySelective(Product record);
//	int updateByPrimaryKey(Product record);
	int selectByPrimaryKey();
	List getProductExtList(int page, int rows);
	
	int insertProduct(Product product);     
	int updateProduct(Product product);	     
	int deleteProduct(Long id);	     
	Product findProductById(Long id);
	Product selectByPrimaryKey(Long id);
	int insert(List<Product> products, String[] acceproductIds,
			String[] acceproductNum, String[] assoproductIds, String[] infoIds);
	int update(List<Product> products, String[] acceproductIds,
			String[] acceproductNum, String[] assoproductIds, String[] infoIds);
	
	//////////////////////////////////////////
	//获取产品列表
	List<Product> getProduct(Product product);
	List<Map<String, String>> getProduct2();
	//获取产品数量
	int getProductCount();
	//通过产品名模糊查询
	List<Product> getProductByInput(String str);
	//通过id删除产品
	int deleteByIds(long parseLong);
	//通过产品名称精确查询
	Product getProduct(String str);
	//按多个产品名称精确查询
	List<Product> getProductByNames(String[] str);
	//根据产品类型查询产品
	List<Product> getProductByTypeName(String name);

	

}
