package com.trent.sparker.service.commands;

import com.trent.sparker.service.SparkOptions;

import java.io.IOException;

public class UIModuleCommand extends Command {

	UIModuleCommand(String executionPath, String commandString, SparkOptions sparkOptions) {
		super(executionPath, commandString, sparkOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		System.out.println("Creating ui module... \n");
		super.run();
	}
}
