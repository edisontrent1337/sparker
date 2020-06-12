package com.trent.sparker.service.commands.app;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.service.commands.Command;
import com.trent.sparker.utils.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		Files.createDirectories(Paths.get(root.toString(), projectName + ".app", "musl"));


		Path targetDockerfile = Paths.get(root.toString(), projectName + ".app", "Dockerfile");
		DataUtils.copyTemplateFileToLocation("template_dockerfile", targetDockerfile);

		Path targetMuslFile = Paths.get(root.toString(), projectName + ".app", "musl", "ld-musl-x86_64.path");
		DataUtils.copyTemplateFileToLocation("ld-musl-x86_64.path", targetMuslFile);

		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("app_pom", sparkerOptions);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString()
				+ "/"
				+ projectName
				+ ".app/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();

		if (sparkerOptions.isFlywayEnabled()) {
			new FlyWayCommand(this.executionPath, this.commandString, this.sparkerOptions).run();
		}
	}
}
