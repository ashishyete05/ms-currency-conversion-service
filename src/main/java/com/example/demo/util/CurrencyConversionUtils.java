package com.example.demo.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyConversionUtils {

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
