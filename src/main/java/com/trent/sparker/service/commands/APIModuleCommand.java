package com.trent.sparker.service.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.utils.DataUtils;

public class APIModuleCommand extends Command {
	public APIModuleCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName, projectName + ".api");
		Files.createDirectories(root);
		InputStream apiFileStream = getClass().getClassLoader().getResourceAsStream("templates/template_api.yaml");
		if (apiFileStream == null) {
			throw new IOException("api.yaml file not found.");
		}
		Files.copy(apiFileStream, Paths.get(root.toString(), "api.yaml"));
		System.out.println("Creating api module... \n");
		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("api_pom", sparkerOptions);
		super.run();
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString() + "/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}

}
