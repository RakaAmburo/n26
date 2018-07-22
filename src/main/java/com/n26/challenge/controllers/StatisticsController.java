package com.n26.challenge.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.n26.challenge.entities.Statistics;
import com.n26.challenge.services.StatisticsService;

@RestController
public class StatisticsController {
	
	@Autowired
	StatisticsService service;
	
	@GetMapping("/statistics")
	public Statistics statistics() {
		return service.getStatistics();
	}

}
