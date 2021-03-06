package com.qvik;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableScheduling
public class MessageLoggingApplication {
	
	private static final Logger logger = LogManager.getLogger(MessageLoggingApplication.class.getName());
	
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;
	
	public static void main(String[] args) {
		SpringApplication.run(MessageLoggingApplication.class, args);
	}
	
	
	@Scheduled(cron = "${cron.expression}")
	public void perform() throws Exception {
		logger.info("Perform called ********************");
		JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(job, params);
	}
}
