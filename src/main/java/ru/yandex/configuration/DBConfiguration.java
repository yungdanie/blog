package ru.yandex.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @EventListener
    public void populate(ContextRefreshedEvent event) {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("/db/schema.sql"));
        resourceDatabasePopulator.execute(dataSource);
    }

}
