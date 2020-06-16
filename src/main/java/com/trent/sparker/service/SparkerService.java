package com.trent.sparker.service;

import java.io.IOException;

import com.trent.sparker.service.commands.Command;
import org.springframework.stereotype.Service;

@Service
public class SparkerService {

	public void create(SparkerOptions sparkerOptions) {

		try {
			createParentModule(sparkerOptions);
			createAppModule(sparkerOptions);
			createAPIModule(sparkerOptions);
			createWebModule(sparkerOptions);
			createGitRepository(sparkerOptions);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void createGitRepository(SparkerOptions sparkerOptions) {
		try {
			Command.createGitCommand(sparkerOptions).run();
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

	void createParentModule(SparkerOptions sparkerOptions) throws IOException, InterruptedException {
		Command.createParentModuleCommand(sparkerOptions).run();
	}

	void createAppModule(SparkerOptions sparkerOptions) throws IOException, InterruptedException {
		Command.createAppModuleCommand(sparkerOptions).run();
	}

	void createAPIModule(SparkerOptions sparkerOptions) throws IOException, InterruptedException {
		Command.createAPIModuleCommand(sparkerOptions).run();
	}

	void createWebModule(SparkerOptions sparkerOptions) throws IOException, InterruptedException {
		Command.createWebModuleCommand(sparkerOptions).run();
	}

}
