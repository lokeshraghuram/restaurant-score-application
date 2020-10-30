package com.nhs.inspection.restaurantscoring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JDBCDatasourceConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTemplate createJDBCTemplate() {
        return new JdbcTemplate(dataSource, false);
    }
}