package com.trent.sparker.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class SparkerOptions extends Options {

	private String projectName;
	private String groupId;
	private String artifactId;
	private Path basePath;
	private String language;
	private String mainClass;

	public SparkerOptions() {
		this.basePath = Paths.get("").toAbsolutePath();
		this.language = "java";
		this.groupId = "group-id";
		this.artifactId = "artifact-id";
		this.mainClass = "MyMainClass";
		createOption("basePath", "The path where the project should be created.");
		createOption("language", "The language of the project. Can be java or kotlin.");
		createOption("projectName", "The name of the project.");
		createOption("groupId", "The group id complying with the maven naming conventions.");
		createOption("artifactId", "The artifact id.");
		createOption("mainClass", "The desired name of the main class.");
		createOption("runAsServer", "Flag to run this application as a server.");
		createOption("help", "Shows the help dialog.");
	}

	private void createOption(String option, String description) {
		addOption(Option
				.builder()
				.longOpt(option)
				.valueSeparator()
				.hasArg(true)
				.desc(description).build());
	}

	public String getProjectName() {
		return projectName;
	}

	public SparkerOptions(CommandLine commandLine) {
		super();
		this.basePath = Paths.get(commandLine.getOptionValue("basePath"));
		this.groupId = commandLine.getOptionValue("groupId");
		this.projectName = commandLine.getOptionValue("projectName");
		this.artifactId = commandLine.getOptionValue("artifactId");
		this.language = commandLine.getOptionValue("language");
		this.mainClass = commandLine.getOptionValue("mainClass");
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

}
