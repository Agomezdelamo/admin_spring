package spring.security.service;

import spring.security.model.User;

public interface UserService {
	
	/**
	 * Método que encuentra al usuario por id
	 * @param id
	 * @return
	 */
	User findById(int id);
	
	/**
	 * Método que encuentra al usuario por su username
	 * @param id
	 * @return
	 */
	User findBySso(String sso);
	
}