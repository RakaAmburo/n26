package com.n26.challenge.services;

import org.springframework.stereotype.Service;

import com.n26.challenge.models.Statistics;

@Service
public class StatisticsService {

	public Statistics getStatistics() {

		Statistics s = new Statistics();
		s.setSum(Double.valueOf(100));
		s.setAvg(Double.valueOf(100));
		s.setMax(Double.valueOf(100));
		s.setMin(Double.valueOf(100));
		s.setCount(Long.valueOf(10));

		return s;
	}

}
