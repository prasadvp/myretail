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
			+ "|| execution (* com.myretail.catalog.service..*(..))"
			+ "|| execution (* com.myretail.catalog.persistence.dao..*(..))")
	
	 public Object instrument(ProceedingJoinPoint joinPoint) throws Throwable {
		 
		LOGGER.info("LoggingAspect::Inside logging ");
		 Object retVal = null;
		 long startTime = System.currentTimeMillis();
		 long elapsedTime;
		 StringBuilder logMessage = new StringBuilder();
		 
		try {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			logMessage.append(joinPoint.getTarget().getClass().getName());
			logMessage.append(".");
			logMessage.append(signature.getName());
			
			String logMsg = logMessage.toString();
			LOGGER.info("BEGIN EXECUTION :{}",logMsg);
			retVal = joinPoint.proceed();
			elapsedTime = System.currentTimeMillis() - startTime;
			
			LOGGER.info("END EXECUTION : {}  Execution time {} ms" , logMsg , elapsedTime );
			
			
		} catch (Exception e) {
			LOGGER.info("Exception occurred while instrumenting {} ", e.getMessage());
		}
		 
		 return retVal;
		 
	 }
}
