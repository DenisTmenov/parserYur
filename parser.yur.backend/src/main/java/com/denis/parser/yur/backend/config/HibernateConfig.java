package com.denis.parser.yur.backend.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.denis.parser.yur.backend.utils.PropertiesClass;

@Configuration
@ComponentScan(basePackages = { "com.denis.parser.yur.backend.dto" })
@EnableTransactionManagement
public class HibernateConfig {

	private Properties properties;
	private static String dbNameConfig;

	private static final String DTO = "com.denis.parser.yur.backend.dto";
	private static final String H_DIALECT = "hibernate.dialect";
	private static final String H_SHOW_SQL = "hibernate.show_sql";
	private static final String H_FORMAT_SQL = "hibernate.format_sql";
	private static final String TRUE = "true";

	private static final String P_NAME_CONFIG = "db.name.config";
	private static final String P_USE_DB = "usingDb";
	private static final String P_DRIVER = "db.driver";
	private static final String P_URL = "db.url";
	private static final String P_USER = "db.user";
	private static final String P_PASSWORD = "db.password";
	private static final String P_SSL = "db.ssl";

	private static final String USE_SSL = "useSSL=";

	static {
		dbNameConfig = PropertiesClass.getSettings(P_USE_DB).getProperty(P_NAME_CONFIG);
	}

	private static class DataSourceHolder {
		private static final HibernateConfig HOLDER_INSTANCE = new HibernateConfig();
	}

	public static HibernateConfig getInstance() {
		return DataSourceHolder.HOLDER_INSTANCE;
	}

	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	@Bean("dataSource")
	public DataSource getDataSource() {
		properties = PropertiesClass.getSettings(dbNameConfig);

		BasicDataSource basicDS = new BasicDataSource();

		basicDS.setDriverClassName(properties.getProperty(P_DRIVER));
		basicDS.setUrl(properties.getProperty(P_URL));
		basicDS.setUsername(properties.getProperty(P_USER));
		basicDS.setPassword(properties.getProperty(P_PASSWORD));

		if (properties.getProperty(P_SSL) != null) {
			basicDS.setConnectionProperties(USE_SSL + properties.getProperty(P_SSL));
		}

		return basicDS;
	}

	@Bean
	public SessionFactory getSessionFactory(DataSource dataSource) {

		LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource);

		builder.addProperties(getHibernateProperties());
		builder.scanPackages(DTO);

		return builder.buildSessionFactory();
	}

	private Properties getHibernateProperties() {
		properties.put(H_DIALECT, properties.getProperty(H_DIALECT));
		properties.put(H_SHOW_SQL, TRUE);
		properties.put(H_FORMAT_SQL, TRUE);

		return properties;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager(sessionFactory);
		return hibernateTransactionManager;
	}

}