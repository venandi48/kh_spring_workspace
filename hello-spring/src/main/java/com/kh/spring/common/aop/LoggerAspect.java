package com.kh.spring.common.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * #11.1. Aspect작성 (pointcut + advice)
 */
@Component
@Aspect
@Slf4j
public class LoggerAspect {
	
	/**
	 * com.kh.spring.todo패키지 하위의 모든 클래스 모든 메소드
	 *  - 매개변수 상관없음
	 *  - 리턴타입 상관없음
	 */
	@Pointcut("execution(* com.kh.spring.todo..*(..))")
	public void pointcut() {}


	/**
	 * joinpoint 앞,뒤에서 실행되는 Around Advice
	 */
	@Around("pointcut()")
	public Object logger(ProceedingJoinPoint joinPoint) throws Throwable {
		Signature signature = joinPoint.getSignature();
		String type = signature.getDeclaringTypeName(); // 클래스명
		String method = signature.getName(); // 메소드명

		// before
		log.debug("[Before] {}.{}", type, method);

		Object returnObj = joinPoint.proceed();

		// after
		log.debug("[After] {}.{}", type, method);

		return returnObj;
	}

}
