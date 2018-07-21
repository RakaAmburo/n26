package com.n26.challenge.services;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.n26.challenge.entities.Statistics;
import com.n26.challenge.entities.Transaction;
import com.n26.challenge.services.exceptions.TransactionReportException;

@Service
public class TransactionService {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");
	private final Queue<Transaction> transactions = new LinkedBlockingQueue<Transaction>();
	private volatile Statistics stats = new Statistics();

	@Value("${time.range}")
	private long timeRangeInSeconds;

	public void reportTransaction(Transaction transaction) {

		validateTransTime(transaction);
		transactions.add(transaction);

		double amount = transaction.getAmount();
		double sum = stats.getSum() + amount;
		long count = stats.getCount() + 1;
		double avg = (stats.getAvg() * stats.getCount() + amount) / count;
		double max = Math.max(stats.getMax(), amount);
		double min = Math.min(stats.getMin(), amount);

		stats = new Statistics(sum, avg, max, min, count);
	}

	private void validateTransTime(Transaction transaction) {

		Instant transactionTime = Instant.ofEpochMilli(transaction.getTimestamp());
		Instant timeLimit = Instant.now().minusSeconds(timeRangeInSeconds);
		Date date = new Date(Instant.now().toEpochMilli());
		FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
		LOGGER.info("Adding trans: {} / {}", Instant.now().toEpochMilli(), FORMATTER.format(date));
		if (transactionTime.isBefore(timeLimit)) {
			Duration between = Duration.between(transactionTime, timeLimit);
			throw new TransactionReportException(between.getSeconds());
		}
	}

	private boolean isOutDated(Transaction transaction) {

		Instant transactionTime = Instant.ofEpochMilli(transaction.getTimestamp());
		Instant timeLimit = Instant.now().minusSeconds(timeRangeInSeconds);
		return transactionTime.isBefore(timeLimit);
	}

	public void removeInvalidTransactions() {

		Transaction lastValidTransaction = transactions.peek();
		int removedTransactions = 0;
		while (lastValidTransaction != null && isOutDated(lastValidTransaction)) {
			LOGGER.info("Removing trans (HH:mm:ss): {}", lastValidTransaction);
			transactions.poll();
			removedTransactions++;
			lastValidTransaction = transactions.peek();
		}

		evaluateNewStatistics();

		if (removedTransactions == 0) {
			LOGGER.info("No transactions to remove!");
		}
	}

	private void evaluateNewStatistics() {
		DoubleSummaryStatistics newStatistics = transactions.stream().mapToDouble(Transaction::getAmount)
				.summaryStatistics();
		int count = transactions.size();
		double sum = newStatistics.getSum();
		double avg = newStatistics.getAverage();
		double max = newStatistics.getMax();
		double min = newStatistics.getMin();
		stats = new Statistics(sum, avg, max, min, count);
	}
	
	public Statistics getStats() {
		return stats;
	}

}
