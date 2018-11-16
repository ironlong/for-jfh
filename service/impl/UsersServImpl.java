package com.shop.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shop.dao.entity.Users;
import com.shop.dao.itf.AddressDbMapper;
import com.shop.dao.itf.UsersMapper;

import com.shop.service.itf.IUsersServ;
@Service("UsersServImpl")
public class UsersServImpl implements IUsersServ{
	private UsersMapper usersMapper;
	private AddressDbMapper addressDbMapper;
	 
	public UsersMapper getUsersMapper() {
		return usersMapper;
	}
	@Autowired
	public void setUsersMapper(UsersMapper usersMapper) {
		this.usersMapper = usersMapper;
	}
////////////////////////////////////////////////////////////////////
	@Override
	public int getUsersCount() {
		// TODO Auto-generated method stub
		return usersMapper.getUsersCount();
	}
	@Override
	public List<Users> getUsers(Users users) {
		users.computeStart();
		return usersMapper.getUsers(users.getStart(),users.getRows());
	}
	@Override
	public int deleteByIds(Long id) {
		// TODO Auto-generated method stub
		return usersMapper.deleteByIds(id);
	}
	@Override
	public int insert(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String title = request.getParameter("title");
		String mgContent = request.getParameter("managize");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
		Users users = new Users();
		//Users.setManagizeId(SerialNumberUtil.getSerialNumber());
		//users.setManagizeTitle(title);
		//users.setManagizeContent(mgContent);
		//users.setCreattime(df.format(new Date()));
		return usersMapper.insert(users);
	}
	@Override
	public Users selectByPrimaryKey(Long usersId) {
		// TODO Auto-generated method stub
		return usersMapper.selectByPrimaryKey(usersId);
	}
	@Override
	public int updateByPrimaryKeyWithBLOBs(Users users) {
		// TODO Auto-generated method stub
		return usersMapper.updateByPrimaryKey(users);
	}
	@Override
	public int buildQueue(Users users, String groupId) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
		/**
		MemberMailQueue queue = new MemberMailQueue();
		queue.setQueueId(SerialNumberUtil.getSerialNumber());
		queue.setManagizeId(managize.getManagizeId());
		queue.setGroupId(groupId);
		queue.setCreattime(df.format(new Date()));
		queue.setIsSend("0");
		queue.setDr(0l);
		*/
		return usersMapper.insert(users);
	}
	@Override
	public List<Users> getUsersByInput(String str) {
		// TODO Auto-generated method stub
		return usersMapper.getUsersByInput(str);
	}
	@Override
	public Users getUsersByUsersName(String usersName) {
		// TODO Auto-generated method stub
		return usersMapper.getUsersByUsersName(usersName);
	}
	@Override
	public int insertUsers(Users users) {
		// TODO Auto-generated method stub
		return usersMapper.insert(users);
	}
	@Override
	public Users getUsersDetailByUsersId(Long usersId) {
		return usersMapper.getUsersDetailByUsersId(usersId);
	}
	@Override
	public List<Map<String, String>> getAddressProvince() {
		// TODO Auto-generated method stub
		return addressDbMapper.getAddressProvince();
	}
	@Override
	public List<Map<String, String>> getAddressCity(int province) {
		// TODO Auto-generated method stub
		return addressDbMapper.getAddressCity(province);
	}
	

}
