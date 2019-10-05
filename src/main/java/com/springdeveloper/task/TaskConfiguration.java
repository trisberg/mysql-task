package com.springdeveloper.task;

import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableTask
@Configuration
public class TaskConfiguration {

	@Bean
	public SelectTask selectTask() {
		return new SelectTask();
	}
}
