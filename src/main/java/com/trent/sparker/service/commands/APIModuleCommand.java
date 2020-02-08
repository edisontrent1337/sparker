package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;
import com.trent.sparker.utils.DataUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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
		URL apiFileLocation = getClass().getClassLoader().getResource("templates/template_api.yaml");
		if (apiFileLocation == null) {
			throw new IOException("api.yaml file not found.");
		}
		Path yamlFile = new File(apiFileLocation.getFile()).toPath();
		Files.copy(yamlFile, Paths.get(root.toString(), "api.yaml"));
		System.out.println("Creating api module... \n");
		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("api_pom", sparkOptions);
		super.run();
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}

}
