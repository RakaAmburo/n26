package com.n26.challenge.config;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.n26.challenge.services.TransactionService;

@Component
public class TransactionRemoval {

	@Autowired
	TransactionService transactionService;

	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");

	private static final Logger LOGGER = LogManager.getLogger();

	@Scheduled(fixedRateString = "${removal.fixed.rate}")
	public void removeTransactions() {
		LOGGER.info("Removing outdated transactions:");
		FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date(Instant.now().toEpochMilli());
		LOGGER.info("Now (HH:mm:ss): {}", FORMATTER.format(date));
		transactionService.removeInvalidTransactions();
	}

}
