package com.nhs.inspection.restaurantscoring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${application.auth.security.enabled:false}")
    private boolean securityEnabled;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        if (securityEnabled)
            httpSecurity.csrf().disable().authorizeRequests().anyRequest().authenticated().and().oauth2ResourceServer().jwt();
        else
            httpSecurity.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        String issuer = "https://icow.accounts.ingka.com/";
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);
        // Validate the jwt token here;
        return jwtDecoder;
    }
}