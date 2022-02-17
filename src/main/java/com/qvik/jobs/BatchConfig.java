package com.qvik.jobs;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qvik.Repository.LogMessaging;
import com.qvik.Repository.LogMessagingImpl;

 
@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer{
     
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
    private LogMessaging repository;
     
    @Bean
    public Step stepOne(){
        return steps.get("stepOne")
                .tasklet(new LogMessagingImpl(repository.getAllLogs()))
                .build();
    }
     
    @Bean
    public Job demoJob(){
        return jobs.get("logJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .build();
    }
}