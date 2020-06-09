package com.trent.sparker.service.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;

public class Command {

	SparkerOptions sparkerOptions;
	private String commandString;
	private String executionPath;

	public Command(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		this.executionPath = executionPath;
		this.commandString = commandString;
		this.sparkerOptions = sparkerOptions;
	}

	public static HelpCommand createHelpCommand(SparkerOptions sparkerOptions) {
		Path root = getRootFromOptions(sparkerOptions);
		return new HelpCommand(root.toString(), "", sparkerOptions);
	}

	public static AppModuleCommand createAppModuleCommand(SparkerOptions sparkerOptions) {
		String projectName = sparkerOptions.getProjectName();
		String language = sparkerOptions.getLanguage();
		Path root = getRootFromOptions(sparkerOptions);
		return new AppModuleCommand(root.toString(),
				" curl https://start.spring.io/starter.tgz -d dependencies=web,actuator \\\n" +
						" -d language=" + language +
						" -d type=maven-project" +
						" -d name=" + sparkerOptions.getProjectName() +
						" -d groupId=" + sparkerOptions.getGroupId() +
						" -d artifactId=" + sparkerOptions.getArtifactId() +
						" -d baseDir=" + projectName + ".app" +
						" | tar -xzvf -",
				sparkerOptions);
	}

	public static UIModuleCommand createUIModuleCommand(SparkerOptions sparkerOptions) {
		String projectName = sparkerOptions.getProjectName();
		Path root = getRootFromOptions(sparkerOptions);
		return new UIModuleCommand(root.toString(), "npx create-react-app " + projectName + ".ui", sparkerOptions);
	}

	public static APIModuleCommand createAPIModuleCommand(SparkerOptions sparkerOptions) {
		Path root = getRootFromOptions(sparkerOptions);
		return new APIModuleCommand(root.toString(), "", sparkerOptions);
	}

	public static ParentModuleCommand createParentModuleCommand(SparkerOptions sparkerOptions) {
		Path root = getRootFromOptions(sparkerOptions);
		return new ParentModuleCommand(root.toString(), "", sparkerOptions);
	}

	public static GitCommand createGitCommand(SparkerOptions sparkerOptions) {
		Path root = getRootFromOptions(sparkerOptions);
		String projectName = sparkerOptions.getProjectName();
		System.out.println("Initializing git repository...");
		return new GitCommand(root.toString(), "rm -rf ./" + projectName + ".ui/.git && git init", sparkerOptions);
	}

	private static Path getRootFromOptions(SparkerOptions sparkerOptions) {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		return Paths.get(sparkerOptions.getBasePath().toString(), sparkerOptions.getProjectName());
	}

	public void run() throws IOException, InterruptedException {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		Files.createDirectories(root);
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
}
