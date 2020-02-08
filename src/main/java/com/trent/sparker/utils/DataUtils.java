package com.trent.sparker.utils;

import com.trent.sparker.service.SparkOptions;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataUtils {

	public static String getTemplateXMLAsString(String fileName) throws IOException {
		if (!fileName.endsWith(".xml")) {
			fileName += ".xml";
		}
		InputStream is = DataUtils.class.getClassLoader().getResourceAsStream("templates/" + fileName);
		if (is == null) {
			throw new IOException("File " + fileName + " was not found");
		}
		return IOUtils.toString(is, StandardCharsets.UTF_8);
	}

	public static String populateTemplateFileWithOptions(String templateFile, SparkOptions sparkOptions) throws IOException {
		String rawTemplate = getTemplateXMLAsString(templateFile);
		String result = rawTemplate.replaceAll("\\{projectName}", sparkOptions.getProjectName())
				.replaceAll("\\{groupId}", sparkOptions.getGroupId())
				.replaceAll("\\{artifactId}", sparkOptions.getArtifactId())
				.replaceAll("\\{mainClass}", sparkOptions.getMainClass());
		return result;
	}

	public static void createFile(Path root, String fileName) throws IOException {
		String destination = root.toString() + "/" + fileName;
		System.out.println(destination);
		Files.createFile(Paths.get(destination));
	}
}
