package com.kh.spring.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration : Bean설정이 가능한 클래스
 * @Bean 메소드. 리턴값이 SpringContainer가 관리할 Bean객체
 */
@Configuration
public class UserConfig {
	
	/**
	 * 메소드명이 bean의 id
	 */
	@Bean
	public UserController userController() {
		return new UserController(userService());
	}
	
	@Bean
	public UserService userService() {
		return new UserService();
	}
}
