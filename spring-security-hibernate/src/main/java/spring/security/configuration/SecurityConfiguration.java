package spring.security.configuration;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
 
/**
 * Generamos un servlet que se encarga de gestionar la seguridad de la aplicación.
 * @author Dj nezhod
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	/**
	 * Servicio para la autenticación en base de datos
	 */
    @Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService userDetailsService;
    
    /**
     * Método público que se autoinyecta al construir este servlet, recibe un constructor para managers de autenticación y a ese constructor le setea nuestro servicio de user details
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
    
     /**
      * Método que recoge un objeto httpSecurity que procede a autorizar las request que venga una vez pasen por el sensor,
      * y mapea que hacer con cada una, si el path de la request contiene tal cadena el acceso esta permitido solo para tal rol.
      */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
        .antMatchers("/", "/home").permitAll()
        .antMatchers("/admin/**").access("hasRole('ADMIN')")
        .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
        .and().formLogin().loginPage("/login") //setea la pagina de login en este contexto de seguridad
        .usernameParameter("ssoId").passwordParameter("password") //setea los parametros de login y password
        .and().csrf() //no lo se
        .and().exceptionHandling().accessDeniedPage("/Access_Denied"); //setea la pagina de acceso denegado
    }
}