package com.trent.sparker.service.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.utils.DataUtils;

public class AppModuleCommand extends Command {

	AppModuleCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		String projectName = sparkerOptions.getProjectName();
		Path basePath = sparkerOptions.getBasePath();
		Path root = Paths.get(basePath.toString(), projectName);
		Files.createDirectories(root);

		System.out.println("Creating app module... \n");
		super.run();
		Files.createDirectories(Paths.get(root.toString(), projectName + ".app", "musl"));

		InputStream templateDockerFileStream = getClass().getClassLoader()
				.getResourceAsStream("templates/template_dockerfile");
		if (templateDockerFileStream == null) {
			throw new IOException("Template dockerfile file not found.");
		}
		Path dockerfile = Paths.get(root.toString(), projectName + ".app", "Dockerfile");
		Files.createFile(dockerfile);
		Files.copy(templateDockerFileStream, dockerfile, StandardCopyOption.REPLACE_EXISTING);

		InputStream muslFileStream = getClass().getClassLoader().getResourceAsStream("templates/ld-musl-x86_64.path");
		if (muslFileStream == null) {
			throw new IOException("Musl directory not found.");
		}
		Files.copy(muslFileStream, Paths.get(root.toString(), projectName + ".app", "musl", "ld-musl-x86_64.path"));

		String rawTemplatePOM = DataUtils.populateTemplateFileWithOptions("app_pom", sparkerOptions);
		BufferedWriter writer = new BufferedWriter(new FileWriter(root.toString()
				+ "/"
				+ projectName
				+ ".app/pom.xml"));
		writer.write(rawTemplatePOM);
		writer.close();
	}
}
