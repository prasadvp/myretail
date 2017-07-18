package com.myretail.catalog.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.myretail.catalog.exception.RetailServiceException;
import com.myretail.catalog.helper.RetailConstants;

@Configuration
@EnableWebSecurity

public class CustomWebSecurityAdapter extends WebSecurityConfigurerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomWebSecurityAdapter.class);
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)  {
		try {
			auth.authenticationProvider(new CustomAuthProvider());
		} catch (Exception e) {
			LOGGER.error("Exception during Authenticating request  "+ e.getMessage());
			throw new RetailServiceException(RetailConstants.DEFAULT_EXCEPTION.getCode(), RetailConstants.DEFAULT_EXCEPTION.getDescription(),e.getMessage());
		}
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
  
      http.csrf().disable()
        .authorizeRequests().antMatchers(HttpMethod.GET).permitAll().anyRequest().authenticated()
        .and().exceptionHandling().authenticationEntryPoint(getBasicAuthEntryPoint())
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//We don't need sessions to be created.
      
      http.addFilterBefore(new CustomAuthFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }
	
	@Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new CustomBasicAuthenticationEntryPoint();
    }
}
