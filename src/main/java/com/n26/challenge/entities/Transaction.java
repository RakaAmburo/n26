package com.n26.challenge.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Transaction {
	
	private double amount;
	private long timestamp;
	
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
    public String toString() {
		Date date = new Date(timestamp);
		FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
        return FORMATTER.format(date);
    }

}
