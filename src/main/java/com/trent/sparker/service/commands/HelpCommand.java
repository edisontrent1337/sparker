package com.trent.sparker.service.commands;

import java.io.IOException;

import com.trent.sparker.service.SparkerOptions;
import org.apache.commons.cli.HelpFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelpCommand extends Command {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelpCommand.class);

	public HelpCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		super.run();
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("java -jar sparker-X.X.X-SNAPSHOT.jar", this.sparkerOptions);
	}
}
