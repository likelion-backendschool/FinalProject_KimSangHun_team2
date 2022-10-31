package com.ebook.multbooks.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job orderJob(Step orderStep, CommandLineRunner initData) throws Exception {

        //Job 실행전 데이터 초기화
        initData.run();

        return jobBuilderFactory.get("orderJob")
                .start(orderStep)
                .build();
    }

    @Bean
    @JobScope
    public Step orderStep(Tasklet orderTasklet){
        return stepBuilderFactory.get("orderStep")
                .tasklet(orderTasklet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet orderTasklet(){
        return (contribution, chunkContext) -> {
            log.debug("orderTasklet 실행됨");

            return RepeatStatus.FINISHED;
        };
    }
}
