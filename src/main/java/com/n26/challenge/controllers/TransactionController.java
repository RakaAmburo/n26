package com.n26.challenge.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.services.exceptions.TransactionReportException;
import com.n26.challenge.models.Transaction;
import com.n26.challenge.services.TransactionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
public class TransactionController {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	TransactionService service;
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
	public void transactions (@RequestBody Transaction t) {
    	service.reportTransaction(t);
	}
    
    @ExceptionHandler(TransactionReportException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleException(TransactionReportException e) {
    	LOGGER.error(e.getMessage());
    }

}
