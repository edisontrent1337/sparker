package com.trent.sparker.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparkerOptions extends Options {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkerOptions.class);

	private String projectName;
	private String groupId;
	private String artifactId;
	private Path basePath;
	private String language;
	private String mainClass;
	private boolean isFlywayEnabled;

	public SparkerOptions() {
		this.basePath = Paths.get("").toAbsolutePath();
		this.language = "java";
		this.groupId = "group-id";
		this.artifactId = "artifact-id";
		this.mainClass = "MyMainClass";
		this.projectName = "my-project";
		createOption("basePath", true, "The path where the project should be created.");
		createOption("language", true, "The language of the project. Can be java or kotlin.");
		createOption("projectName", true, "The name of the project.");
		createOption("groupId", true, "The group id complying with the maven naming conventions.");
		createOption("artifactId", true, "The artifact id.");
		createOption("mainClass", true, "The desired name of the main class.");
		createOption("runAsServer", true, "Flag to run this application as a server.");
		createOption("help", false, "Shows the help dialog.");
		createOption("flyway", false, "Generates files for flyway.");
	}

	private void createOption(String option, boolean hasArg, String description) {
		addOption(Option
				.builder()
				.longOpt(option)
				.valueSeparator()
				.hasArg(hasArg)
				.desc(description).build());
	}

	public String getProjectName() {
		return projectName;
	}

	public SparkerOptions(CommandLine commandLine) {
		super();
		this.basePath = Paths.get(setOptionFromCommandLine("basePath", commandLine, ""));
		this.groupId = setOptionFromCommandLine("groupId", commandLine, "group-id");
		this.projectName = setOptionFromCommandLine("projectName", commandLine, "my-project");
		this.artifactId = setOptionFromCommandLine("artifactId", commandLine, "artifact-id");
		this.language = setOptionFromCommandLine("language", commandLine, "java");
		this.isFlywayEnabled = commandLine.hasOption("--flyway");
		this.mainClass = setOptionFromCommandLine("mainClass", commandLine, "MyMainClass");
	}

	private String setOptionFromCommandLine(String option, CommandLine commandLine, String defaultValue) {
		if (commandLine.getOptionValue(option) != null) {
			return commandLine.getOptionValue(option);
		}
		return defaultValue;
	}

	public SparkerOptions setProjectName(String projectName) {
		this.projectName = projectName;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public SparkerOptions setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public SparkerOptions setArtifactId(String artifactId) {
		this.artifactId = artifactId;
		this.mainClass = artifactId.substring(0, 1).toUpperCase() + artifactId.substring(1) + "Application";
		return this;
	}

	public Path getBasePath() {
		return basePath;
	}

	public SparkerOptions setBasePath(Path basePath) {
		this.basePath = basePath;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public SparkerOptions setLanguage(String language) {
		this.language = language;
		return this;
	}

	public String getMainClass() {
		return mainClass;
	}

	public SparkerOptions setFlyWay(boolean isFlywayEnabled) {
		this.isFlywayEnabled = isFlywayEnabled;
		return this;
	}

	public boolean isFlywayEnabled() {
		return isFlywayEnabled;
	}
}
