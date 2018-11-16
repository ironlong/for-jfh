package com.shop.service.impl.shiro;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.dao.entity.User;
import com.shop.dao.itf.UserMapper;
import com.shop.service.itf.shiro.IShiroAuth;

@Service("ShiroAuth")
public class ShiroAuth implements IShiroAuth {
	
	private UserMapper userMapper;

	private SessionDAO sessionDAO;

//	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public User findSubject(String principals) {
        return getUserMapper().selectUserExtByUserName(principals);
    }
    
    
    
    public UserMapper getUserMapper() {
		return userMapper;
	}
	@Autowired
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}



	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}


	@Autowired
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}



	@Override
	public int letUserOut(String loginName) {

		for(Session session:getAllSessions()){//循环遍历当前登录的所有用户

		if(loginName.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {

			session.setTimeout(0);//设置session立即失效，即将其踢出系统
			break;

		}

		}
		return 1;
	}



	@Override
	public Collection<Session> getAllSessions() {
		Collection<Session> sessions = sessionDAO.getActiveSessions();

		for(Session session:sessions){

		System.out.println("登录ip:"+session.getHost());

		System.out.println("登录用户"+session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));

		System.out.println("最后操作日期:"+session.getLastAccessTime());

		}
		return sessions;
	}
}
