package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppModuleCommand extends Command {

	AppModuleCommand(String executionPath, String commandString, SparkOptions sparkOptions) {
		super(executionPath, commandString, sparkOptions);
	}
	@Override
	public void run() throws IOException, InterruptedException {
		String projectName = sparkOptions.getProjectName();
		Path basePath = sparkOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		System.out.println("Creating app module... \n");
		Files.createDirectories(root);
		super.run();
	}
}
