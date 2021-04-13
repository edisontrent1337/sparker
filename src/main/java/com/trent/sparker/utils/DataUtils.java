package com.trent.sparker.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;

import com.trent.sparker.service.SparkerOptions;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUtils.class);

	public static String readTemplateFileAsString(String fileName) throws IOException {
		return readResourceFileAsString("templates/" + fileName);
	}

	public static String readFileAsString(String fileName) throws IOException {
		File file = new File(fileName);
		InputStream is = new FileInputStream(file);
		return IOUtils.toString(is, StandardCharsets.UTF_8);
	}

	public static String readResourceFileAsString(String fileName) throws IOException {
		InputStream is = DataUtils.class.getClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			throw new IOException("File " + fileName + " was not found");
		}
		return IOUtils.toString(is, StandardCharsets.UTF_8);
	}

	public static String populateTemplateFileWithOptions(String templateFile, SparkerOptions sparkerOptions)
			throws IOException {
		String rawTemplate = readTemplateFileAsString(templateFile);
		String databaseName = sparkerOptions.getArtifactId().replaceAll("-", "_").toLowerCase();
		if (sparkerOptions.shouldSkipWebModule()) {
			rawTemplate = rawTemplate.replaceAll("<module>\\{projectName}.web</module>", "");
		}
		if (sparkerOptions.shouldSkipApiModule()) {
			rawTemplate = rawTemplate.replaceAll("<module>\\{projectName}.api</module>", "")
					.replaceAll(Pattern.quote("\t\t\t<!-- API sub module dependency -->\n" +
							"\t\t\t<dependency>\n" +
							"\t\t\t\t<groupId>{groupId}</groupId>\n" +
							"\t\t\t\t<artifactId>{artifactId}.api</artifactId>\n" +
							"\t\t\t\t<version>${project.version}</version>\n" +
							"\t\t\t</dependency>"), "");
		}
		return rawTemplate.replaceAll("\\{projectName}", sparkerOptions.getProjectName())
				.replaceAll("\\{groupId}", sparkerOptions.getGroupId())
				.replaceAll("\\{artifactId}", sparkerOptions.getArtifactId())
				.replaceAll("\\{databaseName}", databaseName)
				.replaceAll("\\{mainClass}", sparkerOptions.getMainClass());
	}

	public static void createFile(Path root, String fileName) throws IOException {
		String destination = root.toString() + "/" + fileName;
		System.out.println(destination);
		Files.createFile(Paths.get(destination));
	}

	public static void copyTemplateFileToLocation(String templateFileName, Path targetLocation) throws IOException {
		InputStream templateFileStream = DataUtils.class.getClassLoader()
				.getResourceAsStream("templates/" + templateFileName);
		if (templateFileStream == null) {
			String errorMessage = String.format("Template file %s not found.", templateFileName);
			LOGGER.error(errorMessage);
			throw new IOException(errorMessage);
		}
		LOGGER.info("Copying file {} to location {}.", templateFileName, targetLocation);
		Files.createFile(targetLocation);
		Files.copy(templateFileStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
	}

	public static void writeStringToFile(String content, String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		writer.write(content);
		writer.close();
	}
}