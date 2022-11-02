package com.ebook.multbooks.job;

import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.orderItem.repository.OrderItemRepository;
import com.ebook.multbooks.app.rebate.entity.RebateOrderItem;
import com.ebook.multbooks.app.rebate.repository.RebateOrderItemRepository;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RebateOrderItemJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrderItemRepository orderItemRepository;
    private final RebateOrderItemRepository rebateOrderItemRepository;


    @Bean
    public Job makeRebateOrderItemJob(Step orderStep, CommandLineRunner initData) throws Exception {

        //Job 실행전 데이터 초기화
        initData.run();

        return jobBuilderFactory.get("makeRebateOrderItemJob")
                .start(orderStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step orderStep(
            ItemReader orderItemReader,
            ItemProcessor orderItemToRebateOrderItemProcessor,
            ItemWriter rebateOrderItemWriter
            ) {
        return stepBuilderFactory.get("orderStep")
                .<OrderItem, RebateOrderItem>chunk(100)
                .reader(orderItemReader(null))
                .processor(orderItemToRebateOrderItemProcessor)
                .writer(rebateOrderItemWriter)
                .build();
    }


    @StepScope
    @Bean
    public RepositoryItemReader<OrderItem> orderItemReader(
            @Value("#{jobParameters[yearMonth]}") String yearMonth
            ) {
            String fromDateStr= Util.date.getBeforeYearMonth(yearMonth)+"-15 00:00:00.000000";
            String toDateStr=yearMonth+"-15 23:59:59.999999";
            LocalDateTime fromDate=Util.date.parse(fromDateStr);
            LocalDateTime toDate=Util.date.parse(toDateStr);
            return new RepositoryItemReaderBuilder<OrderItem>()
                    .name("orderItemReader")
                    .repository(orderItemRepository)
                    .methodName("findAllByPayDateBetween")
                    .pageSize(100)
                    .arguments(Arrays.asList(fromDate,toDate))
                    .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                    .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<OrderItem, RebateOrderItem> orderItemToRebateOrderItemProcessor() {
        return orderItem -> new RebateOrderItem(orderItem);
    }

    @StepScope
    @Bean
    public ItemWriter<RebateOrderItem> rebateOrderItemWriter() {
        return items -> items.forEach(item -> {
            RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItem(item.getOrderItem()).orElse(null);

            if (oldRebateOrderItem != null) {
                oldRebateOrderItem.update(item);
                rebateOrderItemRepository.save(oldRebateOrderItem);
            }else{
                rebateOrderItemRepository.save(item);
            }
        });
    }
}