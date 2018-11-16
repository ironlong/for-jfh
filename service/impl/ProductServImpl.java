package com.shop.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.Product;
import com.shop.dao.itf.ProductMapper;
import com.shop.service.itf.IProductServ;

@Service("IProductServ")
public class ProductServImpl implements IProductServ{
	
	private ProductMapper productMapper;
	
	
	
	public ProductMapper getProductMapper() {
		return productMapper;
	}
	@Autowired
	public void setProductMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}
 
	@Override	
	public int deleteProduct(Long id) {
		// TODO Auto-generated method stub
		return productMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertProduct(Product product) {
		// TODO Auto-generated method stub
		return productMapper.insert(product);
	}

	/*@Override
	public int insertProduct1(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}*/

	@Override
	public Product selectByPrimaryKey(Long id) {
		
		return productMapper.selectByPrimaryKey(id);
	}

	/*@Override
	public int updateProduct(Product product) {
		
		return 0;
	}*/

	/*@Override
	public int updateByPrimaryKey(Product record) {
		// TODO Auto-generated method stub
		return 0;
	}*/
	@Override
	public int selectByPrimaryKey() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List getProductExtList(int page, int rows) {
		// TODO Auto-generated method stub
		return getProductExtList(page, rows);
	}
	/*@Override
	public int insertProduct(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}*/
	@Override
	public int updateProduct(Product product) {
		// TODO Auto-generated method stub
		return productMapper.updateByPrimaryKey(product);
	}
	/*@Override
	public int deleteProduct1(String id) {
		// TODO Auto-generated method stub
		return 0;
	}*/
	@Override
	public Product findProductById(Long id) {
		// TODO Auto-generated method stub
		return productMapper.selectByPrimaryKey(id);
	}
	@Override
	public int insert(List<Product> products, String[] acceproductIds,
			String[] acceproductNum, String[] assoproductIds, String[] infoIds) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int update(List<Product> products, String[] acceproductIds,
			String[] acceproductNum, String[] assoproductIds, String[] infoIds) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<Product> getProduct(Product product) {
		// TODO Auto-generated method stub
		product.computeStart();
		return productMapper.getProduct(product.getStart(),product.getRows());
	}
	@Override
	public List<Map<String, String>>  getProduct2() {
		// TODO Auto-generated method stub
		return productMapper.getProduct2();
	}
	@Override
	public int getProductCount() {
		// TODO Auto-generated method stub
		return productMapper.getProductCount();
	}
	@Override
	public List<Product> getProductByInput(String str) {
		// TODO Auto-generated method stub
		return productMapper.getProductByInput(str);
	}
	@Override
	public int deleteByIds(long parseLong) {
		// TODO Auto-generated method stub
		return productMapper.deleteByIds(parseLong);
	}
	@Override
	public List<Product> getProductByNames(String[] str) {
		// TODO Auto-generated method stub
		return productMapper.getProductByNames(str);
	}
	@Override
	public Product getProduct(String str) {
		// TODO Auto-generated method stub
		return productMapper.getProductByName(str);
	}
	@Override
	public List<Product> getProductByTypeName(String name) {
		// TODO Auto-generated method stub
		return productMapper.getProductByTypeName(name);
	}
	
}
