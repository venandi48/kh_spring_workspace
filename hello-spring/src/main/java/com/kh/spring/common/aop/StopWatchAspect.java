package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class StopWatchAspect {

	@Pointcut("execution(* com.kh.spring..insertTodo(..))")
	public void stopWachPointcut() {
	}

	@Around("stopWachPointcut()")
	public Object stopWatch(ProceedingJoinPoint joinPoint) throws Throwable {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object returnObj = joinPoint.proceed();

		stopWatch.stop();
		log.debug("insertTodo 실행시간 = {} ", stopWatch.prettyPrint());

		return returnObj;
	}
}
