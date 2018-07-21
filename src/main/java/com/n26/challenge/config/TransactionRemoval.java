package com.n26.challenge.config;

import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.n26.challenge.services.TimeCustomFormat;
import com.n26.challenge.services.TransactionService;

@Component
public class TransactionRemoval {

	@Autowired
	TransactionService transactionService;

	@Autowired
	TimeCustomFormat tcf;

	private static final Logger LOGGER = LogManager.getLogger();

	@Scheduled(fixedRateString = "${removal.fixed.rate}")
	public void removeTransactions() {
		LOGGER.info("Removing outdated transactions:");
		LOGGER.info("Now (HH:mm:ss): {}", tcf.getFormattedTime(Instant.now()));
		transactionService.removeInvalidTransactions();
	}

}
