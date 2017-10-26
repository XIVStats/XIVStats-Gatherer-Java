package com.ffxivcensus.gatherer.spring;

import java.io.IOException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.Gatherer;
import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class SpringConfiguration {

    @Bean
    public ApplicationConfig applicationConfig() throws ParserConfigurationException, IOException, SAXException {
        return ConfigurationBuilder.createBuilder()
                                   .loadXMLConfiguration()
                                   .getConfiguration();
    }

    @Bean
    @Scope("prototype") // This is a prototype bean, as we want a new one every time
    public Gatherer gatherer() {
        return new Gatherer();
    }

    @Bean(destroyMethod="close")
    @Lazy // Assume lazy here so that this doesn't get created before the CLI options have been applied
    public DataSource dataSource() throws ParserConfigurationException, IOException, SAXException {
        ApplicationConfig appConfig = applicationConfig();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:" + appConfig.getDbUrl() + "/" + appConfig.getDbName());
        hikariConfig.setUsername(appConfig.getDbUser());
        hikariConfig.setPassword(appConfig.getDbPassword());
        hikariConfig.setMaximumPoolSize(appConfig.getThreadLimit());
        hikariConfig.setInitializationFailTimeout(30000);
        if(appConfig.isDbIgnoreSSLWarn()) {
            hikariConfig.addDataSourceProperty("useSSL", false);
        }

        return new HikariDataSource(hikariConfig);
    }
}
