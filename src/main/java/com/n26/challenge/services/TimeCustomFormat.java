package com.n26.challenge.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

@Service
public class TimeCustomFormat {
	
	
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");
	
	public TimeCustomFormat() {
		FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public String getFormattedTime(Instant instant) {		
		Date date = new Date(instant.toEpochMilli());
		return FORMATTER.format(date);
	}

}
