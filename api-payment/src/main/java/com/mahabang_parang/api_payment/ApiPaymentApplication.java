package com.mahabang_parang.api_payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mahabang_parang", "com.mahabang_parang.api_billing"})
public class ApiPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiPaymentApplication.class, args);
	}

}
