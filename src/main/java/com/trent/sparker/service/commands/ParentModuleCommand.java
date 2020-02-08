package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;
import com.trent.sparker.utils.DataUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParentModuleCommand extends Command {
	public ParentModuleCommand(String executionPath, String commandString, SparkOptions sparkOptions) {
		super(executionPath, commandString, sparkOptions);
	}

	@Override
	public void run() throws IOException {
		System.out.println("Creating directory structure... \n");
		String projectName = sparkOptions.getProjectName();
		Path basePath = sparkOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		Files.createDirectories(root);
		Path pomLocation = Paths.get(basePath.toString(), projectName);
		DataUtils.createFile(pomLocation, "pom.xml");
		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("main_pom", sparkOptions);
		System.out.println(rawTemplatePOM);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}
}
