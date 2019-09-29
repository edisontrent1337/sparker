package com.trent.sparker.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SparkService {

	private Path basePath;

	public void create(String projectName) {
		if (basePath == null) {
			throw new IllegalStateException("Please specify the base path where the project should be created");
		}

		Path root = Paths.get(basePath.toString(), projectName);
		try {
			Files.createDirectories(root);
			Command command = new Command(root.toString()," curl https://start.spring.io/starter.tgz -d dependencies=web,actuator \\\n" +
			"-d language=java -d type=maven-project -d baseDir=" + projectName + ".app | tar -xzvf -");
			command.run();
			Command other = new Command(root.toString(), "npx create-react-app " +projectName + ".ui");
			other.run();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setBasePath(Path basePath) {
		this.basePath = basePath;
	}
}
