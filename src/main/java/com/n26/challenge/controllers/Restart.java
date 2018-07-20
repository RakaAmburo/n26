package com.n26.challenge.controllers;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Restart {
	
	@Value("${spring.devtools.restart.trigger-file}")
	private String file;

	@GetMapping("/restart")
	public String retrieveAllStudents() {
		File f = new File(file);
		f.setLastModified(System.currentTimeMillis());
		return "Restarting app";
	}

}
