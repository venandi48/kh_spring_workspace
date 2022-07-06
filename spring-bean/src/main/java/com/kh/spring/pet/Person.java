package com.kh.spring.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Person {

	/**
	 * Pet타입의 빈을 찾아서 의존주입
	 *  - 필드에 주입 (private필드도 가능)
	 */
//	@Autowired
	private Pet pet;

	public Person() {
		System.out.println("Person 객체 생성!");
	}

//	@Autowired
//	@Qualifier("dog") // The annotation @Qualifier is disallowed for this location
	public Person(Pet pet) {
		System.out.println("Person 객체 생성 & 의존 주입 : " + pet);
		this.pet = pet;
	}

	public Pet getPet() {
		return pet;
	}

	@Autowired
	@Qualifier("dog")
	public void setPet(Pet pet) {
		System.out.println("Pet 의존 주입 : " + pet);
		this.pet = pet;
	}

	@Override
	public String toString() {
		return "Person [pet=" + pet + "]";
	}

}
