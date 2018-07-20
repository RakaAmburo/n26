package com.n26.challenge.services.exceptions;

public class TransactionReportException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TransactionReportException(String message) {
		super(message);
	}

}
