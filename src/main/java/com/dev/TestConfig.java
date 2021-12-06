package com.dev;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test config that turn on H2 in-memory database.
 * This mode is more convenient for fast starting.
 * Change property spring.profiles.active to "test" for run application in this mode.
 */

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:election;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.jdbc.batch_size", 50);
        hibernateProperties.put("hibernate.connection.characterEncoding", "utf8");
        hibernateProperties.put("hibernate.cache.use_second_level_cache", "true");
        hibernateProperties.put("hibernate.cache.use_query_cache", "true");
        hibernateProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        sessionFactoryBean.setMappingResources("objects.hbm.xml");
        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @DependsOn("h2WebServer")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers");
    }

}
