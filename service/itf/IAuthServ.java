package com.shop.service.itf;

public interface IAuthServ {
	public void login(String username,String password,boolean isRemember,String IP);
	public void logout();
}
