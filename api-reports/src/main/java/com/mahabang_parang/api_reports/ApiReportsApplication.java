package com.mahabang_parang.api_reports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mahabang_parang", "com.mahabang_parang.api_billing", "com.mahabang_parang.api_payment"})
public class ApiReportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiReportsApplication.class, args);
	}

}
