package com.ebook.multbooks.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobScheduler {
    private final JobLauncher jobLauncher;
    private final RebateOrderItemJobConfig rebateOrderItemJobConfig;
    private final Job makeRebateOrderItemJob;
    private String yearMonth;
    @Scheduled(cron = " 0 0 4 15 * ?")
    public void runJob(){
        String yearMonth= LocalDateTime.now().getYear()+"-"+LocalDateTime.now().getMonthValue();
        Map<String, JobParameter> confMap=new HashMap<>();
        confMap.put("yearMonth",new JobParameter(yearMonth));
        JobParameters jobParameters=new JobParameters(confMap);
        try {
            jobLauncher.run(makeRebateOrderItemJob,jobParameters);
        }catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

            log.error(e.getMessage());
        }

    }
}
