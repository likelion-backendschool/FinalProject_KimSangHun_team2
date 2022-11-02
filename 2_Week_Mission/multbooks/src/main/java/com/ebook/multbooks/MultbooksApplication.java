package com.ebook.multbooks;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling//스케줄링 기능 활성화
@EnableBatchProcessing//배치기능 활성화
@EnableJpaAuditing// Auditing 기능 사용하겠다는 설정
@SpringBootApplication
public class MultbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultbooksApplication.class, args);
	}

}
