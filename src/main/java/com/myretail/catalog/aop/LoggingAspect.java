package com.myretail.catalog.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Around("execution (* com.myretail.catalog.controller..*(..)) "
			+ "|| execution (* com.myretail.catalog.helper..*(..)) "
			+ "|| execution (* com.myretail.catalog.service..*(..))")
	//@Around("execution (* com.myretail.catalog.controller..*(..)) ")
	 public Object instrument(ProceedingJoinPoint joinPoint) throws Throwable {
		 
		System.out.println("Inside logging ");
		 Object retVal = null;
		 long startTime = System.currentTimeMillis();
		 long elapsedTime;
		 StringBuilder logMessage = new StringBuilder();
		 
		try {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			logMessage.append(joinPoint.getTarget().getClass().getName());
			logMessage.append(".");
			logMessage.append(signature.getName());
			
			
			LOGGER.info("BEGIN EXECUTION :" +logMessage.toString());
			retVal = joinPoint.proceed();
			elapsedTime = System.currentTimeMillis() - startTime;
			
			LOGGER.info("END EXECUTION :" + logMessage.toString() + ". Execution time {} ms", elapsedTime );
			
			
		} catch (Exception e) {
			LOGGER.info("Exception occurred while instrumenting {} ", e.getMessage());
		}
		 
		 return retVal;
		 
	 }
}
