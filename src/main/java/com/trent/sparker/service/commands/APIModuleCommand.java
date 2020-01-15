package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class APIModuleCommand extends Command {
	public APIModuleCommand(String executionPath, String commandString, SparkOptions sparkOptions) {
		super(executionPath, commandString, sparkOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		String projectName = sparkOptions.getProjectName();
		Path basePath = sparkOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName, projectName + ".api");
		Files.createDirectories(root);
		System.out.println("Creating api module... \n");
		createFile(root, "api.yml");
		createFile(root, "pom.xml");
		super.run();
	}

	private static void createFile(Path root, String fileName) throws IOException {
		String destination = root.toString()  + "/" +  fileName;
		System.out.println(destination);
		Files.createFile(Paths.get(destination));
	}
}
