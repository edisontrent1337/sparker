package com.trent.sparker.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.service.SparkerOptions;
import org.apache.commons.io.IOUtils;

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

	public static String populateTemplateFileWithOptions(String templateFile, SparkerOptions sparkerOptions)
			throws IOException
	{
		String rawTemplate = getTemplateXMLAsString(templateFile);
		return rawTemplate.replaceAll("\\{projectName}", sparkerOptions.getProjectName())
				.replaceAll("\\{groupId}", sparkerOptions.getGroupId())
				.replaceAll("\\{artifactId}", sparkerOptions.getArtifactId())
				.replaceAll("\\{mainClass}", sparkerOptions.getMainClass());
	}

	public static void createFile(Path root, String fileName) throws IOException {
		String destination = root.toString() + "/" + fileName;
		System.out.println(destination);
		Files.createFile(Paths.get(destination));
	}
}
