package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;
import com.trent.sparker.utils.DataUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
		Files.createDirectories(root);
		System.out.println("Creating app module... \n");
		super.run();
		String rawTemplatePOM = DataUtils.getTemplateXMLAsString("app_pom");
		rawTemplatePOM = rawTemplatePOM
				.replaceAll("\\{projectName}", sparkOptions.getProjectName())
				.replaceAll("\\{groupId}", sparkOptions.getGroupId())
				.replaceAll("\\{artifactId}", sparkOptions.getArtifactId())
				.replaceAll("\\{mainClass}", sparkOptions.getMainClass());
		System.out.println(rawTemplatePOM);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/" + projectName + ".app/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}
}
