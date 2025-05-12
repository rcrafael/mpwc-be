package com.mahabang_parang.api_settings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mahabang_parang"})
public class ApiSettingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiSettingsApplication.class, args);
	}

}
