package com.n26.challenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Statistics {

	private double sum;
	private double avg;
	private double max;
	private double min;
	private long count;
	@JsonIgnore
	private long lastTransactionTime;
	
	public Statistics reset() {
		Statistics oldStats = new Statistics(this.sum, this.avg, this.max, this.min, this.count);
		this.sum = 0;
		this.avg = 0;
		this.max = 0;
		this.min = Double.MAX_VALUE;
		this.count = 0;
		//this.lastTransactionTime = 0;
		oldStats.setLastTransactionTime(this.lastTransactionTime);
		return oldStats;
	}
	
	public Statistics() {
		this.min = Double.MAX_VALUE;
	}

	public Statistics(double sum, double avg, double max, double min, long count) {
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getAvg() {
		return (Double.isNaN(avg))?0:avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getLastTransactionTime() {
		return lastTransactionTime;
	}

	public void setLastTransactionTime(long lastTransactionTime) {
		this.lastTransactionTime = lastTransactionTime;
	}
	
	

}
