package com.n26.challenge.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Concurrent test to verify the functionallity and 
 * the consistency of the transaction and statistics information
 * 
 * @author pablo.paparini
 *
 */
public class ConcurrentTest {

	public static void main(String[] args) {

		ExecutorService executor = Executors.newFixedThreadPool(6);

		Callable<Integer> task = () -> {
			try {

				try {

					while (true) {

						URL url = new URL("http://10.226.111.49:8095/n26/transactions");
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

						Thread.sleep(generateRandomIntBet(300, 600));
					}

				} catch (IOException e) {
					System.out.println("Connection error posting transaction");
				}
				return 123;
			} catch (InterruptedException e) {
				throw new IllegalStateException("task interrupted", e);
			}
		};

		executor.submit(task);
		executor.submit(task);
		executor.submit(task);
		executor.submit(task);
		executor.submit(task);

		Callable<Integer> task2 = () -> {
			try {

				try {

					int outInLine = 0;
					
					while (true) {

						URL url = new URL("http://10.226.111.49:8095/n26/statistics");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						//conn.setDoOutput(true);
						conn.setRequestMethod("GET");
						outInLine ++;
						try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
						   if (outInLine % 30 == 0) {
							   System.out.println("OK ");
						   } else {
							   System.out.print("OK ");
						   }
                           
                           
						} catch (Exception e) {
							System.out.println("problem reading stats");
						}

						if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
							System.out.println("Response error getting stats");
						}
						conn.disconnect();

						Thread.sleep(1000);
					}

				} catch (IOException e) {
					System.out.println("Connection error getting stats");
				}
				return 123;
			} catch (InterruptedException e) {
				throw new IllegalStateException("task2 interrupted", e);
			}
		};
		
		//executor.submit(task2);
		executor.submit(task2);

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
