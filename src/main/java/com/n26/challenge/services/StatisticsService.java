package com.n26.challenge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.challenge.entities.Statistics;

@Service
public class StatisticsService {
	
	@Autowired
	TransactionService transactionService;

	public Statistics getStatistics() {

		return transactionService.getStats();
	}

}
