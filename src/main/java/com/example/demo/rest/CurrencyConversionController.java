package com.example.demo.rest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.bean.CurrencyConversionValue;
import com.example.demo.proxy.CurrencyExchangeProxy;

@RestController
public class CurrencyConversionController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	CurrencyExchangeProxy exchangeProxy;

	private static String url = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionValue calculateCurrencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		logger.info("calculateCurrencyConversion method called from {} to {} for quantity{}",from,to,quantity);
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<CurrencyConversionValue> responseEntity = restTemplate.getForEntity(url,
				CurrencyConversionValue.class, uriVariables);
		CurrencyConversionValue currencyConversionVal = responseEntity.getBody();

		return new CurrencyConversionValue(currencyConversionVal.getId(), from, to, quantity,
				currencyConversionVal.getConversionMultiple(),
				quantity.multiply(currencyConversionVal.getConversionMultiple()),
				currencyConversionVal.getEnvironment()+" -> restTemplate");
	}

	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionValue calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		CurrencyConversionValue currencyConversionVal = exchangeProxy.retrieveExchangeValue(from, to);

		return new CurrencyConversionValue(currencyConversionVal.getId(), from, to, quantity,
				currencyConversionVal.getConversionMultiple(),
				quantity.multiply(currencyConversionVal.getConversionMultiple()),
				currencyConversionVal.getEnvironment()+" -> feignClient");
	}

}
