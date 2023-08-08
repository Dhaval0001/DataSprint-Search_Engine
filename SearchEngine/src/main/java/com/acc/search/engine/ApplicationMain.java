package com.acc.search.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.acc.search.engine")
//@ComponentScan({ "com.acc.search.engine.controller" })
public class ApplicationMain {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
	}
	
	@Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(10);
        pool.setMaxPoolSize(10);
        pool.setThreadNamePrefix("task-Thread-");
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

}
