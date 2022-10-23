package com.ebook.multbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing// Auditing 기능 사용하겠다는 설정
@SpringBootApplication
public class MultbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultbooksApplication.class, args);
	}

}
