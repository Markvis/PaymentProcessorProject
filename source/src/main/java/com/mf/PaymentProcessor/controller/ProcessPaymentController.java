package com.mf.PaymentProcessor.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.mf.PaymentProcessor.businessLogic.ProcessPaymentBL;
import com.mf.PaymentProcessor.databean.CreditCardBean;
import com.mf.PaymentProcessor.databean.TransactionConfirmationBean;

@RestController
public class ProcessPaymentController {

	@PostMapping("/api/auth")
	String post(@RequestBody String request) {
		
		Gson gson = new Gson();
		CreditCardBean creditCardDataBean = gson.fromJson(request, CreditCardBean.class);
		ProcessPaymentBL processPaymentBL = new ProcessPaymentBL(creditCardDataBean);
		TransactionConfirmationBean transactionConfirmationBean = processPaymentBL.process();
		
		return gson.toJson(transactionConfirmationBean, TransactionConfirmationBean.class);
	}

}
