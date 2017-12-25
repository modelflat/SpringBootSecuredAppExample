package com.github.modelflat.SpringBootSecuredAppExample.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // all requests to path matching /user/{name}/** require to be logged in as "user"
                // concrete endpoint configuration could be done using @PreAuthorize
                .authorizeRequests().antMatchers("/user/{name}/**").access(
                        "hasRole('ADMIN') or " +
                                "(hasRole('USER') and @webSecurity.checkUserId(authentication, #name))"
                ).and()
                // max sessions per user is 1; send user to /login?expired when session expired
                .sessionManagement().maximumSessions(1).expiredUrl("/login?expired")
                    // migrate session on destroy
                    .and().sessionFixation().migrateSession().and()
                // use form-login with /login as processing URL.
                // spring will generate login page automagically
                .formLogin().permitAll().and()
                // use CSRF, not HTTP-only / todo commented during dev
                // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrf().disable()
        ;
    }

    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService( mongoUserDetailsService )
                .passwordEncoder( new BCryptPasswordEncoder() )
        ;
    }

}
