package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Command {

	private String commandString;
	SparkOptions sparkOptions;
	private String executionPath;

	public Command(String executionPath, String commandString, SparkOptions sparkOptions) {
		this.executionPath = executionPath;
		this.commandString = commandString;
		this.sparkOptions = sparkOptions;
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
			output.append(line).append("\n");
		}

		int exitVal = process.waitFor();
		if (exitVal == 0) {
			System.out.println("Success!");
			System.out.println(output);
		}
	}

	public static AppModuleCommand createAppModuleCommand(SparkOptions sparkOptions) {
		String projectName = sparkOptions.getProjectName();
		String language = sparkOptions.getLanguage();
		Path root = getRootFromOptions(sparkOptions);
		return new AppModuleCommand(root.toString(), " curl https://start.spring.io/starter.tgz -d dependencies=web,actuator \\\n" +
				" -d language=" + language +
				" -d type=maven-project" +
				" -d groupId=" + sparkOptions.getGroupId() +
				" -d artifactId=" + sparkOptions.getArtifactId() +
				" -d baseDir=" + projectName + ".app" +
				" | tar -xzvf -", sparkOptions);
	}

	public static UIModuleCommand createUIModuleCommand(SparkOptions sparkOptions) {
		String projectName = sparkOptions.getProjectName();
		Path root = getRootFromOptions(sparkOptions);
		return new UIModuleCommand(root.toString(), "npx create-react-app " + projectName + ".ui", sparkOptions);
	}

	public static APIModuleCommand createAPIModuleCommand(SparkOptions sparkOptions) {
		Path root = getRootFromOptions(sparkOptions);
		return new APIModuleCommand(root.toString(), "", sparkOptions);
	}

	private static Path getRootFromOptions(SparkOptions sparkOptions) {
		String projectName = sparkOptions.getProjectName();
		Path basePath = sparkOptions.getBasePath();
		return Paths.get(basePath.toString(), projectName);

	}
}
