package com.trent.sparker.service.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.service.commands.api.APIModuleCommand;
import com.trent.sparker.service.commands.app.AppModuleCommand;
import com.trent.sparker.service.commands.web.WebModuleCommand;

public class Command {

	protected SparkerOptions sparkerOptions;
	protected String commandString;
	protected String executionPath;

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
				" curl https://start.spring.io/starter.zip -d dependencies=web,actuator \\\n" +
						" -d language=" + language +
						" -d type=maven-project" +
						" -d name=" + sparkerOptions.getProjectName() +
						" -d groupId=" + sparkerOptions.getGroupId() +
						" -d artifactId=" + sparkerOptions.getArtifactId() +
						" -d baseDir=" + projectName + ".app" + " -o temp.zip && unzip temp.zip && rm -f temp.zip",
				sparkerOptions);
	}

	public static WebModuleCommand createWebModuleCommand(SparkerOptions sparkerOptions) {
		String projectName = sparkerOptions.getProjectName();
		Path root = getRootFromOptions(sparkerOptions);
		return new WebModuleCommand(root.toString(), "npx create-react-app " + projectName + ".web --template typescript", sparkerOptions);
	}

	public static APIModuleCommand createAPIModuleCommand(SparkerOptions sparkerOptions) {
		Path root = getRootFromOptions(sparkerOptions);
		return new APIModuleCommand(root.toString(), "", sparkerOptions);
	}

	public static ParentModuleCommand createParentModuleCommand(SparkerOptions sparkerOptions) {
		Path root = getRootFromOptions(sparkerOptions);
		return new ParentModuleCommand(root.toString(), "git init", sparkerOptions);
	}

	public static GitCommand createGitCommand(SparkerOptions sparkerOptions) {
		Path root = getRootFromOptions(sparkerOptions);
		String projectName = sparkerOptions.getProjectName();
		System.out.println("Initializing git repository...");
		return new GitCommand(root.toString(),
				"rm -rf ./"
						+ projectName
						+ ".web/.git && git init && git add . && git commit -m 'Initial commit created by Sparker.'",
				sparkerOptions);
	}

	private static Path getRootFromOptions(SparkerOptions sparkerOptions) {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		return Paths.get(basePath.toString(), projectName);
	}

	public void run() throws IOException, InterruptedException {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		Files.createDirectories(root);
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.redirectErrorStream(true);
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

		}
		else {
			System.out.println("Failed to create web module.");
		}
		System.out.println(output);
	}
}
