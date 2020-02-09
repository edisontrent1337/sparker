package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.utils.DataUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UIModuleCommand extends Command {

	UIModuleCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName, projectName + ".ui");
		Files.createDirectories(root);
		System.out.println("Creating ui module... \n");
		super.run();
		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("ui_pom", sparkerOptions);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}
}
