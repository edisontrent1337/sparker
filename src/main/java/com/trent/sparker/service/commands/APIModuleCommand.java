package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
		createFile(root, "api_pom.xml");
		createFile(root, "pom.xml");
		readTemplate();
		super.run();
	}

	private void readTemplate() throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("templates/api_pom.xml");
		String rawTemplatePOM = IOUtils.toString(is, StandardCharsets.UTF_8);
		rawTemplatePOM = rawTemplatePOM
				.replaceAll("\\{groupId}", sparkOptions.getGroupId())
				.replaceAll("\\{artifactId}", sparkOptions.getArtifactId());
		System.out.println(rawTemplatePOM);
	}

	private static void createFile(Path root, String fileName) throws IOException {
		String destination = root.toString()  + "/" +  fileName;
		System.out.println(destination);
		Files.createFile(Paths.get(destination));
	}
}
