package com.ffxivcensus.gatherer.spring;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.CLIConstants;
import com.ffxivcensus.gatherer.Console;
import com.ffxivcensus.gatherer.GatheringStatus;
import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;
import com.ffxivcensus.gatherer.task.GathererTask;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class SpringConfiguration {

    @Bean
    public ApplicationConfig applicationConfig() throws ParserConfigurationException, IOException, SAXException, ParseException {
        return ConfigurationBuilder.createBuilder()
                                   .loadXMLConfiguration()
                                   .loadCommandLineConfiguration(CLIConstants.setupOptions(), Console.getARGS())
                                   .getConfiguration();
    }

    @Bean
    @Scope("prototype") // This is a prototype bean, as we want a new one every time
    public GathererTask gatherer() {
        return new GathererTask();
    }

    @Bean(destroyMethod = "close") // Should happen anyway, but worth calling out
    @Primary
    public HikariDataSource dataSource() throws ParserConfigurationException, IOException, SAXException, ParseException {
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

    @Bean
    public GatheringStatus gatheringStatus() {
        return new GatheringStatus();
    }
}
