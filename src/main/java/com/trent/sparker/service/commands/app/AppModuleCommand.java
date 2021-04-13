package com.trent.sparker.service.commands.app;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.service.commands.Command;
import com.trent.sparker.utils.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppModuleCommand extends Command {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppModuleCommand.class);

	public AppModuleCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		Files.createDirectories(root);

		LOGGER.info("Creating app module... \n");
		super.run();
		String moduleFolderName = projectName + ".app";
		Files.createDirectories(Paths.get(root.toString(), moduleFolderName, "musl"));

		Path targetDockerfile = Paths.get(root.toString(), moduleFolderName, "Dockerfile");
		DataUtils.copyTemplateFileToLocation("template_dockerfile", targetDockerfile);

		Path targetMuslFile = Paths.get(root.toString(), moduleFolderName, "musl", "ld-musl-x86_64.path");
		DataUtils.copyTemplateFileToLocation("ld-musl-x86_64.path", targetMuslFile);

		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("app_pom.xml", sparkerOptions);
		String pomFileName = root.toString() + "/" + moduleFolderName + "/pom.xml";
		DataUtils.writeStringToFile(rawTemplatePOM, pomFileName);

		String rawTemplateApplicationYAML = DataUtils.populateTemplateFileWithOptions("template_application.yml",
				sparkerOptions);
		String applicationYAMLFileName = root.toString() + "/" + moduleFolderName + "/src/main/resources/application.yml";
		File applicationPropertiesFile = new File(applicationYAMLFileName);
		applicationPropertiesFile.getParentFile().mkdirs();
		applicationPropertiesFile.createNewFile();
		DataUtils.writeStringToFile(rawTemplateApplicationYAML, applicationYAMLFileName);

		if (sparkerOptions.isFlywayEnabled()) {
			new FlyWayCommand(this.executionPath, this.commandString, this.sparkerOptions).run();
		}
	}
}
