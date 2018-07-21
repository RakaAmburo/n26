package com.n26.challenge.services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	public static void main(String[] args) {
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		Callable<Integer> task = () -> {
		    try {
		        
		    	try {
					
					while (true) {
						
						URL url = new URL("http://192.168.0.26:8095/n26/transactions");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoOutput(true);
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Content-Type", "application/json");

						String input = String.format("{\"amount\":%4.3f,\"timestamp\":%d}", generateDouble(),
								Instant.now().toEpochMilli());

						OutputStream os = conn.getOutputStream();
						os.write(input.getBytes());
						os.flush();

						if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
							System.out.println("Response error adding transactions");
						}
						conn.disconnect();
						
						Thread.sleep(generateRandomIntBet(100, 500));
					}
								
				} catch (IOException e) {
					System.out.println("Connection error posting transaction");
				}
		        return 123;
		    }
		    catch (InterruptedException e) {
		        throw new IllegalStateException("task interrupted", e);
		    }
		};

		executor.submit(task);
		executor.submit(task);
		executor.submit(task);
		executor.submit(task);
		executor.submit(task);

	}
	
	private static int generateRandomIntBet(int a, int b) {
		return new Random().nextInt(a) + b;
	}

	private static double generateDouble() {

		double leftLimit = 1D;
		double rightLimit = 10D;
		double generatedDouble = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
		return generatedDouble;
	}

}
