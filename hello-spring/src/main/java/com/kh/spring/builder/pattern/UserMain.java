package com.kh.spring.builder.pattern;

import java.time.LocalDate;

public class UserMain {

	public static void main(String[] args) {
		
		// 빌더패턴 미사용
		User user = new User("sejong", "1234", null, null, false);
		
		// 빌더패턴 사용
		User user1 = User.builder("honggd", "1234").build();
		
		User user2 = User.builder("sinsa", "1234")
						.birthday(LocalDate.of(1999, 12, 3))
						.address("서울시 강서구")
						.married(true)
						.build();
		
		System.out.println(user1);
		System.out.println(user2);
	}

}
