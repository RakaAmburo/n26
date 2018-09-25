package com.n26.challenge.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.services.exceptions.TransactionReportException;
import com.n26.challenge.entities.Transaction;
import com.n26.challenge.services.TransactionService;

import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
public class TransactionController {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	TransactionService service;
	
    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
	public void transactions (@RequestBody Transaction t) {
    	t.setTimestamp(Instant.now().toEpochMilli());
    	service.reportTransaction(t);
	}
    
    @GetMapping("/listSize")
    public String listSize() {
    	return String.valueOf(service.getListSize());
    }
    
    @ExceptionHandler(TransactionReportException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleException(TransactionReportException e) {
    	LOGGER.error(e.getMessage());
    }

}
