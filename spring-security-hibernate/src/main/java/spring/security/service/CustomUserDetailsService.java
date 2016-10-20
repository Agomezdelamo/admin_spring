package spring.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.security.model.User;
import spring.security.model.UserProfile;

/**
 * Este servicio se proveer los detalles de autenticación al administrador de
 * autenticación
 * 
 * @author Dj nezhod
 *
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	/**
	 * Método que carga los detalles de autenticación por username
	 */
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String ssoId)
			throws UsernameNotFoundException {
		User user = userService.findBySso(ssoId);
		System.out.println("User : " + user);
		if (user == null) {
			System.out.println("User not found");
			throw new UsernameNotFoundException("Username not found");
		}
		/**
		 * devuelve un objeto user de tipo userDetails que en su constructor
		 * recibe el nombre de usuario, la contraseña, el estado y las
		 * autorizaciones que se le van a conceder a ese usuario
		 */
		return new org.springframework.security.core.userdetails.User(
				user.getSsoId(), user.getPassword(), user.getState().equals(
						"Active"), true, true, true,
				getGrantedAuthorities(user));
	}

	/**
	 * Método que devuelve una lista de autorizaciones concedidas a un user
	 * @param user
	 * @return
	 */
	private List<GrantedAuthority> getGrantedAuthorities(User user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		/**
		 * itera por una lista de perfiles de usuario, y los va añadiendo a la lista de autorizaciones que va a devolver
		 */
		for (UserProfile userProfile : user.getUserProfiles()) {
			System.out.println("UserProfile : " + userProfile);
			authorities.add(new SimpleGrantedAuthority("ROLE_"
					+ userProfile.getType()));
		}
		System.out.print("authorities :" + authorities);
		return authorities;
	}

}
