package com.shop.service.impl;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.service.itf.IAuthServ;
import com.shop.service.itf.IUserServ;

@Service("AuthServImpl")
public class AuthServImpl  implements IAuthServ{
	Logger log = Logger.getLogger("R");

	private IUserServ userServ;
	
	public IUserServ getUserServ() {
		return userServ;
	}
	@Autowired
	public void setUserServ(IUserServ userServ) {
		this.userServ = userServ;
	}

	public void login(String username,String password,boolean isRemember,String IP) {
		UsernamePasswordToken upt = new UsernamePasswordToken(username,password);
		try{
 			SecurityUtils.getSubject().login(upt);
			userServ.updateUserLastInfo((String)SecurityUtils.getSubject().getPrincipal(),IP);
		}catch ( UnknownAccountException uae ) {
			throw new UnknownAccountException("用户不存在!");
		} catch ( IncorrectCredentialsException ice ) {
			throw new IncorrectCredentialsException("密码不正确!");
		} catch ( LockedAccountException lae ) {
			throw new LockedAccountException("用户被锁定了,请联系管理员!");
		} catch ( ExcessiveAttemptsException eae ) {
			throw new ExcessiveAttemptsException("尝试次数太多,请联系管理员!");
		}catch(AuthenticationException ae){
			log.error(ae.getMessage());
			throw new AuthenticationException("未知错误,请联系管理员!");
		}
	}

	@Override
	public void logout() {
		SecurityUtils.getSubject().logout();
	}
}
