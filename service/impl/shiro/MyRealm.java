package com.shop.service.impl.shiro;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.dao.entity.Permission;
import com.shop.dao.entity.Role;
import com.shop.dao.entity.User;
import com.shop.service.itf.shiro.IShiroAuth;

@Component("MyRealm")
public class MyRealm extends SimpleAccountRealm {

    protected IShiroAuth shiroAuth;

    public MyRealm() {
        setName("MyRealm");
        PasswordService ps = new DefaultPasswordService();
        PasswordMatcherEx pme = new PasswordMatcherEx();
        pme.setPasswordService(ps);
        setCredentialsMatcher(pme);
    }

    public IShiroAuth getWebAuth() {
        return shiroAuth;
    }

    @Autowired
    public void setWebAuth(IShiroAuth shiroAuth) {
        this.shiroAuth = shiroAuth;
    }

    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upt = (UsernamePasswordToken) token;
        String username = upt.getUsername();
        if (username == null) {
            throw new AccountException("用户名不能为空!");
        }
        User user = shiroAuth.findSubject(username);
        if (user == null) {
            throw new UnknownAccountException("用户为 [" + username + "]不存在!");
        }
        if (Integer.parseInt(user.getLocked()) == 1) {
            throw new LockedAccountException("用户 [" + username + "] 已经被锁定.");
        }
//     SimpleAuthenticationInfo saInfo = new SimpleAuthenticationInfo(username, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
//		RandomNumberGenerator rng = new SecureRandomNumberGenerator();
//		Object salt = rng.nextBytes();
//		System.out.println(salt.toString());
//		String hashedPasswordBase64 = new Sha256Hash("123456", salt, 1024).toBase64();
//        SimpleAuthenticationInfo saInfo = new SimpleAuthenticationInfo(username, user.getPassword(), getName());
        
        //String pwd = new Sha256Hash(getPara("pwd"), getPara("name"), 1024).toBase64(); // 将用户输入密码与用户名salt加密
        return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
    }

    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
       // Long userId = (Long) principals.fromRealm(getName()).iterator().next();
        User user = shiroAuth.findSubject(getUsername(principals));
        if (user instanceof User) {
            SimpleAuthorizationInfo sarpinfo = new SimpleAuthorizationInfo();
            for (Role role : user.getRoles()) {
                sarpinfo.addRole(role.getRoleCode());
                for (Permission p : role.getPermissions()) {
                    sarpinfo.addStringPermission(p.getPermissionCode());
                }
            }
            return sarpinfo;
        } else {
            return null;
        }
    }
}