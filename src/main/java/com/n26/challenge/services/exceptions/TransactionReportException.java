package com.n26.challenge.services.exceptions;

public class TransactionReportException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TransactionReportException(long timeDiff) {
		super(String.format("Transaction out of range for %d seconds", timeDiff));
	}

}
