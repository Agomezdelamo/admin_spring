package spring.security.dao;

import spring.security.model.User;

public interface UserDao {

	User findById(int id);
	
	User findBySSO(String sso);
	
}

