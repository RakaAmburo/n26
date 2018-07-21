package com.n26.challenge.services;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.n26.challenge.entities.Statistics;
import com.n26.challenge.entities.Transaction;
import com.n26.challenge.services.exceptions.TransactionReportException;

public class TransactionServiceTest {// mejorar el date y ordenar lista de trans

	private static final Logger LOGGER = LogManager.getLogger();
	@Test
	public void testReportTransaction() {
		TransactionService transactionService = new TransactionService();
		ReflectionTestUtils.setField(transactionService, "timeRangeInSeconds", 60);
		ReflectionTestUtils.setField(transactionService, "tcf", new TimeCustomFormat());
		List<Transaction> validTransactions = new LinkedList<Transaction>();

		// Valid
		IntStream.rangeClosed(1, 20).forEach(a -> {
			try {
				int randomSecs = generateRandomIntBet(1, 55);
				Transaction t = new Transaction(generateDouble(),
						Instant.now().minusSeconds(randomSecs).toEpochMilli());
				transactionService.reportTransaction(t);
				validTransactions.add(t);
			} catch (TransactionReportException ex) {

			}
		});

		// Invalid
		IntStream.rangeClosed(1, 20).forEach(a -> {
			try {
				int randomSecs = generateRandomIntBet(60, 120);
				Transaction t = new Transaction(generateDouble(),
						Instant.now().minusSeconds(randomSecs).toEpochMilli());
				transactionService.reportTransaction(t);
				validTransactions.add(t);
			} catch (TransactionReportException ex) {
				LOGGER.info("Invalid timestamp");
			}
		});

		Statistics s = transactionService.getStats();
		Statistics myStats = evaluateNewStatistics(validTransactions);

		assertEquals(validTransactions.size(), 20);
		assertEquals(s.getCount(), myStats.getCount(), 0.0001);
		assertEquals(s.getAvg(), myStats.getAvg(), 0.0001);
		assertEquals(s.getMax(), myStats.getMax(), 0.0001);
		assertEquals(s.getMin(), myStats.getMin(), 0.0001);
		assertEquals(s.getSum(), myStats.getSum(), 0.0001);

	}

	private int generateRandomIntBet(int a, int b) {
		return new Random().nextInt(a) + b;
	}

	private double generateDouble() {

		double leftLimit = 1D;
		double rightLimit = 10D;
		double generatedDouble = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
		return generatedDouble;
	}

	private Statistics evaluateNewStatistics(List<Transaction> transactions) {
		DoubleSummaryStatistics newStatistics = transactions.stream().mapToDouble(Transaction::getAmount)
				.summaryStatistics();
		int count = transactions.size();
		double sum = newStatistics.getSum();
		double avg = newStatistics.getAverage();
		double max = newStatistics.getMax();
		double min = newStatistics.getMin();
		return new Statistics(sum, avg, max, min, count);
	}

}
