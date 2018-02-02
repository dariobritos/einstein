package org.proygrad.einstein.persistence.config;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
public class EinsteinPersistenceConfig {

    private static final String ENTITIES_PACKAGE = "org.proygrad.einstein.persistence.entities";
    @Autowired
    private Environment env;

    @Bean(name = "dataSource")
    public LazyConnectionDataSourceProxy getDataSource() throws PropertyVetoException {
        PooledDataSource targetDataSource = this.getMainDataSource();
        LazyConnectionDataSourceProxy dataSource = new LazyConnectionDataSourceProxy(targetDataSource);
        return dataSource;
    }

    @Bean(name = "mainDS", destroyMethod = "close")
    public PooledDataSource getMainDataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        dataSource.setDriverClass(env.getRequiredProperty("hibernate.connection.driver_class"));
        dataSource.setJdbcUrl(env.getRequiredProperty("hibernate.connection.url"));
        dataSource.setUser(env.getRequiredProperty("hibernate.connection.username"));
        dataSource.setPassword(env.getRequiredProperty("hibernate.connection.password"));
        dataSource.setMinPoolSize(Integer.parseInt(env.getRequiredProperty("hibernate.c3p0.min_size")));
        dataSource.setMaxPoolSize(Integer.parseInt(env.getRequiredProperty("hibernate.c3p0.max_size")));
        dataSource.setTestConnectionOnCheckin(false);
        dataSource.setIdleConnectionTestPeriod(Integer.parseInt(env.getRequiredProperty("hibernate.c3p0.idle_test_period")));
        dataSource.setPreferredTestQuery("/* PING */ select 1");
        dataSource.setMaxConnectionAge(Integer.parseInt(env.getRequiredProperty("hibernate.c3p0.max_connection_age")));
        dataSource.setMaxStatements(Integer.parseInt(env.getRequiredProperty("hibernate.c3p0.max_statements")));
        dataSource.setMaxStatementsPerConnection(Integer.parseInt(env.getRequiredProperty("hibernate.c3p0.max_statements_per_connection")));
        dataSource.setNumHelperThreads(Integer.parseInt(env.getRequiredProperty("hibernate.c3p0.num_helper_threads")));

        return dataSource;
    }



    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean getLocalSessionFactoryBean() throws PropertyVetoException {
        DataSource dataSource = this.getDataSource();

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.bytecode.use_reflection_optimizer", env.getRequiredProperty("hibernate.bytecode.use_reflection_optimizer"));
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
        hibernateProperties.setProperty("hibernate.generate_statistics", env.getRequiredProperty("hibernate.generate_statistics"));
        hibernateProperties.setProperty("hibernate.connection.autocommit", env.getRequiredProperty("hibernate.connection.autocommit"));
        hibernateProperties.setProperty("hibernate.connection.isolation", "2");
        hibernateProperties.setProperty("hibernate.connection.release_mode", "on_close");


        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setHibernateProperties(hibernateProperties);
        sessionFactory.setPackagesToScan(ENTITIES_PACKAGE);

        return sessionFactory;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor getPersistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }



}
