package com.trent.sparker.service;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SparkOptions {


	private String projectName;
	private String groupId;
	private String artifactId;
	private Path basePath;
	private String language;
	private String mainClass;

	public SparkOptions() {
		this.basePath = Paths.get("").toAbsolutePath();
		this.language = "java";
	}

	public String getProjectName() {
		return projectName;
	}

	public SparkOptions setProjectName(String projectName) {
		this.projectName = projectName;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public SparkOptions setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public SparkOptions setArtifactId(String artifactId) {
		this.artifactId = artifactId;
		this.mainClass = artifactId.substring(0, 1).toUpperCase() + artifactId.substring(1) + "Application";
		return this;
	}

	public Path getBasePath() {
		return basePath;
	}

	public SparkOptions setBasePath(Path basePath) {
		this.basePath = basePath;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public SparkOptions setLanguage(String language) {
		this.language = language;
		return this;
	}

	public String getMainClass() {
		return mainClass;
	}

}
