package com.trent.sparker.service;

import com.trent.sparker.service.commands.*;
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
			createProjectStructure(sparkOptions);
			AppModuleCommand appModuleCommand = Command.createAppModuleCommand(sparkOptions);
			appModuleCommand.run();
			APIModuleCommand apiModuleCommand = Command.createAPIModuleCommand(sparkOptions);
			apiModuleCommand.run();
			UIModuleCommand uiModuleCommand = Command.createUIModuleCommand(sparkOptions);
			uiModuleCommand.run();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void createProjectStructure(SparkOptions sparkOptions) throws IOException {
		Command.createParentModuleCommand(sparkOptions).run();
	}

	public void setBasePath(Path basePath) {
		this.basePath = basePath;
	}
}
