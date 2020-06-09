package com.trent.sparker.service.commands;

import java.io.IOException;

import com.trent.sparker.service.SparkerOptions;

public class GitCommand extends Command {
	public GitCommand(String executionPath, String commandString, SparkerOptions sparkerOptions) {
		super(executionPath, commandString, sparkerOptions);
	}

	@Override
	public void run() throws IOException, InterruptedException {
		super.run();

	}
}
