package com.trent.sparker.service.commands.web;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.service.commands.Command;
import com.trent.sparker.utils.DataUtils;

// TODO Rename to WebModule
public class WebModuleCommand extends Command {

	public WebModuleCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName, projectName + ".web");
		Files.createDirectories(root);
		System.out.println("Creating web module... \n");
		super.run();
		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("web_pom", sparkerOptions);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}
}
