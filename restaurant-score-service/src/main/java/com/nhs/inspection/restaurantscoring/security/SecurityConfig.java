package com.nhs.inspection.restaurantscoring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.nhs.inspection.restaurantscoring.security.ApplicationUserRole.INSPECTOR;
import static com.nhs.inspection.restaurantscoring.security.ApplicationUserRole.PUBLIC;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /* Commented section below is template code to perform jwt token validation
    * To keep things simple for implementation, I chose Basic authentication over Jwt Authentication
    */

    /*@Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests().anyRequest().authenticated().and().oauth2ResourceServer().jwt();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        String issuer = "https://abc.com/";
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);
        // Validate the jwt token here;
        return jwtDecoder;
    }
    */

    /* Basic Authentication based on user roles */

    @Override
    public void configure(WebSecurity web)  {
        web.ignoring().antMatchers("/v3/api-docs",
                "/swagger-ui.html",
                "/swagger-ui/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v3/api-docs",
                        "index",
                        "/css/*",
                        "/js/*").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails inspectorUser = User.builder()
                .username("inspector")
                .password(passwordEncoder.encode("inspector"))
                .authorities(INSPECTOR.getGrantedAuthorities())
                .build();

        UserDetails publicUser = User.builder()
                .username("public")
                .password(passwordEncoder.encode("public"))
                .authorities(PUBLIC.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                inspectorUser,
                publicUser
        );

    }
}