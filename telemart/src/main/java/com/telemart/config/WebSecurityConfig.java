package com.telemart.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	JWTAuthenticationFilter jwtAuthenticationFilter;
	
	private static final String[] NON_AUTH_APIS = {"/login","/logout"};
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		System.out.print("configure");
		http.cors().and().csrf().disable().authorizeRequests().antMatchers(NON_AUTH_APIS).permitAll();
		http.headers().contentSecurityPolicy("script-src 'self'");
		http.headers().httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(31536000);
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.csrf().disable();
	
		
	}
	
//	 @Bean
//	    public WebSecurityCustomizer webSecurityCustomizer() {
//	        return (web) -> web.ignoring().antMatchers("/login", "/ignore2");
//	    }

}
