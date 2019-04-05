package com.mf.PaymentProcessor.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mf.PaymentProcessor.controller", "com.mf.PaymentProcessor.support"})
public class PaymentProcessorMod1 {

	public static void main(String[] args) {
		SpringApplication.run(PaymentProcessorMod1.class, args);
	}

}
