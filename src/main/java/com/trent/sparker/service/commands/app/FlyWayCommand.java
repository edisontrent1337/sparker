package com.trent.sparker.service.commands.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.service.commands.Command;

public class FlyWayCommand extends Command {

	public FlyWayCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException {
		System.out.println("Creating flyway files...");
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		Path targetPath = Paths.get(root.toString(),
				projectName + ".app",
				"src",
				"main",
				"resources",
				"db",
				"migration");
		Files.createDirectories(targetPath);
		Files.createFile(Paths.get(targetPath.toString(), "V1__init.sql"));
	}
}
