package com.sync.counter.server.spring;

import com.sync.counter.common.protocol.CounterProtocolContants;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@PropertySource("classpath:app.properties")
@ComponentScan("com.sync.counter")
public class AppConfig implements AsyncConfigurer {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

	@Override
	@Bean
    public ThreadPoolTaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CounterProtocolContants.THREAD_POOL_SIZE);
        executor.setMaxPoolSize(CounterProtocolContants.MAX_POOL_SIZE);
        executor.setQueueCapacity(CounterProtocolContants.QUEUE_SIZE);
        executor.setThreadNamePrefix("CounterExec-");
        executor.initialize();
        return executor;
    }

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new CustomAsyncUncaughtExceptionHandler();
	}
}
