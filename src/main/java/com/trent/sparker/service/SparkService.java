package com.trent.sparker.service;

import com.trent.sparker.service.commands.Command;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class SparkService {

	private Path basePath;

	public void create(SparkOptions sparkOptions) {
		if (basePath == null) {
			throw new IllegalStateException("Please specify the base path where the project should be created");
		}
		try {
			createParentModule(sparkOptions);
			createAppModule(sparkOptions);
			createAPIModule(sparkOptions);
			createUIModule(sparkOptions);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	void createParentModule(SparkOptions sparkOptions) throws IOException {
		Command.createParentModuleCommand(sparkOptions).run();
	}

	void createAppModule(SparkOptions sparkOptions) throws IOException, InterruptedException {
		Command.createAppModuleCommand(sparkOptions).run();
	}

	void createAPIModule(SparkOptions sparkOptions) throws IOException, InterruptedException {
		Command.createAPIModuleCommand(sparkOptions).run();
	}

	void createUIModule(SparkOptions sparkOptions) throws IOException, InterruptedException {
		Command.createUIModuleCommand(sparkOptions).run();
	}

	public void setBasePath(Path basePath) {
		this.basePath = basePath;
	}
}
