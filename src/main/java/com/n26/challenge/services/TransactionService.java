package com.n26.challenge.services;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.n26.challenge.entities.Statistics;
import com.n26.challenge.entities.Transaction;
import com.n26.challenge.services.exceptions.TransactionReportException;

@Service
public class TransactionService {

	private static final Logger LOGGER = LogManager.getLogger();
	private final Queue<Transaction> transactions = new PriorityQueue<Transaction>(Comparator.comparingLong(Transaction::getTimestamp));
	private volatile Statistics stats;

	@Value("${time.range}")
	private long timeRangeInSeconds;
	
	@Autowired
	TimeCustomFormat tcf;

	
	/**
	 * Add transaction to the poll
	 * 
	 * @param transaction
	 */
	public synchronized void reportTransaction(Transaction transaction) {

		validateTransTime(transaction);
		transactions.add(transaction);
		
		if (stats == null) {
			double amount = transaction.getAmount();
			stats = new Statistics(amount, amount, amount, amount, 1);
		} else {
			double amount = transaction.getAmount();
			double sum = stats.getSum() + amount;
			long count = stats.getCount() + 1;
			double avg = (stats.getAvg() * stats.getCount() + amount) / count;
			double max = Math.max(stats.getMax(), amount);
			double min = Math.min(stats.getMin(), amount);

			stats = new Statistics(sum, avg, max, min, count);
		}
	}

	
	/**
	 * External validation for transaction timestamp
	 * 
	 * @param transaction
	 */
	public void validateTransTime(Transaction transaction) {

		Instant transactionTime = Instant.ofEpochMilli(transaction.getTimestamp());
		Instant now = Instant.now();
		Instant timeLimit = now.minusSeconds(timeRangeInSeconds);
		LOGGER.info("Adding trans at: {} / {}", now.toEpochMilli(), tcf.getFormattedTime(now));
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

	
	/**
	 * Remove old timestamp transactions.
	 * Synchronized for data consistency.
	 * 
	 */
	public synchronized void removeInvalidTransactions() {

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
		LOGGER.info("Get stats!");
		return stats;
	}

}
