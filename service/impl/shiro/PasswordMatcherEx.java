package com.shop.service.impl.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.credential.PasswordMatcher;

public class PasswordMatcherEx extends PasswordMatcher{
	@Override  
    protected Object getStoredPassword(AuthenticationInfo storedAccountInfo) {  
        Object stored = super.getStoredPassword(storedAccountInfo);  
        if (stored instanceof char[]) {  
            stored = String.valueOf((char[]) stored);  
        }
        return stored;  
    }  
}