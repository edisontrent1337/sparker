package com.trent.sparker.service.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.utils.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParentModuleCommand extends Command {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParentModuleCommand.class);

	public ParentModuleCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		super.run();
		LOGGER.info("Creating directory structure... \n");
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		Files.createDirectories(root);
		Path parentModuleFolder = Paths.get(basePath.toString(), projectName);
		DataUtils.createFile(parentModuleFolder, "pom.xml");
		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("main_pom.xml", sparkerOptions);
		LOGGER.info(rawTemplatePOM);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();

		DataUtils.copyTemplateFileToLocation("git/template_gitignore",
				Paths.get(parentModuleFolder.toString(), ".gitignore"));

	}
}
