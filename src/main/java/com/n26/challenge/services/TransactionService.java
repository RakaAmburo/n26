package com.n26.challenge.services;

import org.springframework.stereotype.Service;

import com.n26.challenge.services.exceptions.TransactionReportException;
import com.n26.challenge.models.Transaction;

@Service
public class TransactionService {

	public void reportTransaction(Transaction t) {
		
		if (String.valueOf(t.getAmount()).endsWith("1")) {
			throw new TransactionReportException("termination 1");
		} else {
			System.out.println("auditando " + t.getAmount());
		}
		
	}

}
