package com.sync.counter.server.spring;

import java.lang.reflect.Method;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class CustomAsyncUncaughtExceptionHandler implements  AsyncUncaughtExceptionHandler{

	private static final Logger logger = LogManager.getLogger(CustomAsyncUncaughtExceptionHandler.class);

	@Override
	public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
		logger.error(" Error processing worker" );
		arg0.printStackTrace();
	}
}
