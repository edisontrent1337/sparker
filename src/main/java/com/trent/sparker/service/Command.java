package com.trent.sparker.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Command {

	private String commandString;
	private String executionPath;

	public Command(String executionPath, String commandString) {
		this.executionPath = executionPath;
		this.commandString = commandString;
	}

	public void run() throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("bash", "-c", "cd " + this.executionPath + "&& " + commandString);
		StringBuilder output = new StringBuilder();
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");
		}

		int exitVal = process.waitFor();
		if (exitVal == 0) {
			System.out.println("Success!");
			System.out.println(output);
		}
	}

}
