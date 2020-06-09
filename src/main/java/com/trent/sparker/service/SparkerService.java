package com.trent.sparker.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.commands.Command;
import org.springframework.stereotype.Service;

@Service
public class SparkerService {

	public void create(SparkerOptions sparkerOptions) {
		if (sparkerOptions.getProjectName() == null) {
			sparkerOptions.setProjectName("MyProject");
		}
		if (sparkerOptions.getBasePath() == null) {
			sparkerOptions.setBasePath(Paths.get(""));
		}
		if (sparkerOptions.getGroupId() == null) {

		}
		try {
			createParentModule(sparkerOptions);
			createAppModule(sparkerOptions);
			createAPIModule(sparkerOptions);
			createUIModule(sparkerOptions);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void printHelp(SparkerOptions sparkerOptions)
	{
		try {
			Command.createHelpCommand(sparkerOptions).run();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	void createParentModule(SparkerOptions sparkerOptions) throws IOException {
		Command.createParentModuleCommand(sparkerOptions).run();
	}

	void createAppModule(SparkerOptions sparkerOptions) throws IOException, InterruptedException {
		Command.createAppModuleCommand(sparkerOptions).run();
	}

	void createAPIModule(SparkerOptions sparkerOptions) throws IOException, InterruptedException {
		Command.createAPIModuleCommand(sparkerOptions).run();
	}

	void createUIModule(SparkerOptions sparkerOptions) throws IOException, InterruptedException {
		Command.createUIModuleCommand(sparkerOptions).run();
	}

	public void setBasePath(Path basePath) {
	}
}
