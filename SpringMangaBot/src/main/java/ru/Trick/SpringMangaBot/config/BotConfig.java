package ru.Trick.SpringMangaBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableScheduling
@Data
@PropertySource("application.properties")
public class BotConfig {
    private final Environment environment;
    @Autowired
    public BotConfig(Environment environment) {
        this.environment = environment;
    }

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();


        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("driver")));
        dataSource.setUrl(environment.getProperty("url"));
        dataSource.setUsername(environment.getProperty("usernameBD"));
        dataSource.setPassword(environment.getProperty("password"));

        return dataSource;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(){return new JdbcTemplate(dataSource());}

}