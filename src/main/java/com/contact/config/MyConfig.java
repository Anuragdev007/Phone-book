package com.contact.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;






@Configuration

@EnableWebSecurity
public class MyConfig {

	@Autowired
	GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

	@Bean
	public UserDetailsService getDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean

	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
		return authConfiguration.getAuthenticationManager();
	}


	@Bean

	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf)->csrf.disable()).authorizeHttpRequests((auth) -> auth.requestMatchers("/admin/**").hasRole("ADMIN")
        		
                        .requestMatchers("/user/index/**").hasRole("USER").requestMatchers("/**").permitAll()

        ).formLogin(
                form -> {
					try {
						form.loginPage("/signin")
						        .defaultSuccessUrl("/user/success" ,true).and().oauth2Login().loginPage("/signin")
								.successHandler(googleOAuth2SuccessHandler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

        );
		http.authenticationProvider(authenticationProvider());
		

		return http.build();
	}
	


}
