package spring.security.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * La configuración de hibernate contiene @Bean metodos para la DataSource, el SessionFactory y el Transaction Manager.
 * Las Datasource properties estan cogidas del archivo application.properties y contiene los datos de conexión a sql server.
 * @author Dj nezhod
 *
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({ "spring.security.configuration" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {

    @Autowired
    private Environment environment;
    
    // SessioonFactory es un metodo que deveuelve un bean local de sessión de hibernate y 
    // le setea las propiedades, los paquetes a escanear y la fuente de datos
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        //seteamos las propiedades del datasource tirando de su metodo
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "spring.security.model" });
        //seteamos las propiedades de hibernate tirando de su metodo
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
     }
	// dataSource devuelve un objeto de data source con los datos de conexión que seteamos en aplication.properties
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        //le digo que busque en el objeto entorno(enviroment) lap propiedad jdbc.driverClassName que esta definida en un .properties
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }
    
    // hibernate properties devuelve un objeto con las propiedades de hibernate.
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;        
    }
    
    // Devuelve un objeto administrador de la transacción de hibernate, recibe por parametros un objeto sessión factory, 
    // que se le setea al administrador de la transacción.
	@Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }
}

