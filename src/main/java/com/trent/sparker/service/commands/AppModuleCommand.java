package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;
import com.trent.sparker.utils.DataUtils;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

		URL dockerFileLocation = getClass().getClassLoader().getResource("templates/template_dockerfile");
		if (dockerFileLocation == null) {
			throw new IOException("Template dockerfile file not found.");
		}
		Path yamlFile = new File(dockerFileLocation.getPath()).toPath();
		Path dockerfile = Paths.get(root.toString(), projectName + ".app", "Dockerfile");
		Files.createFile(dockerfile);
		Files.copy(yamlFile, dockerfile, StandardCopyOption.REPLACE_EXISTING);

		URL muslFileURL = getClass().getClassLoader().getResource("templates/ld-musl-x86_64.path");
		if (muslFileURL == null) {
			throw new IOException("Musl directory not found.");
		}
		Path muslFile = new File(muslFileURL.getFile()).toPath();
		Files.createDirectories(Paths.get(root.toString(), projectName + ".app", "musl"));
		Files.copy(muslFile, Paths.get(root.toString(), projectName + ".app", "musl", "ld-musl-x86_64.path"));

		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("app_pom", sparkOptions);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/" + projectName + ".app/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}
}
