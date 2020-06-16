package com.trent.sparker.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Paths;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.trent.sparker.SparkerApplication;
import com.trent.sparker.service.commands.HelpCommand;
import com.trent.sparker.support.AbstractSparkerTest;
import com.trent.sparker.support.MemoryLogAppender;
import javax.xml.transform.Source;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparkerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SparkerServiceTest extends AbstractSparkerTest {

	@Test
	public void createProjectWorksCorrectly() throws IOException, InterruptedException {

		SparkerOptions options = createSparkOptions();
		sparkerService.create(options);
		String projectName = options.getProjectName();

		// Check root folder files
		assertParentModuleFiles(options);
		// Check app module files
		assertAppModuleFiles(options);
		// Check ui module files
		assertWebModuleFiles(projectName);
		// Check api module files
		assertAPIModuleFiles(projectName);
		String testFolder = testFolderPath.toString();

		assertGeneratedPomFileIsValid(testFolder + "/project", "main_pom");
		assertGeneratedPomFileIsValid(testFolder + "/project/project.app", "app_pom");
		assertGeneratedPomFileIsValid(testFolder + "/project/project.api", "api_pom");
		assertGeneratedPomFileIsValid(testFolder + "/project/project.web", "ui_pom");

		assertThatBuildWorksCorrectly(options);
	}

	private void assertParentModuleFiles(SparkerOptions options) {
		String projectName = options.getProjectName();
		assertThatFolderExists(projectName);
		assertThatFileExists(projectName, "pom.xml");
		assertThatFileExists(projectName, ".gitignore");
		assertThatFolderExists(projectName, ".git");
	}

	private void assertAPIModuleFiles(String projectName) {
		assertThatFolderExists(projectName, "project.api");
		assertThatFileExists(projectName, "project.api", "pom.xml");
	}

	private void assertAppModuleFiles(SparkerOptions sparkerOptions) {
		String projectName = sparkerOptions.getProjectName();
		String appModuleRoot = projectName + "/" + projectName + ".app";
		assertThatFolderExists(appModuleRoot);
		assertThatFileExists(appModuleRoot, "musl", "ld-musl-x86_64.path");
		assertThatFolderExists(appModuleRoot, "src");
		assertThatFolderExists(appModuleRoot, "src", "main", "java");
		assertThatFolderExists(appModuleRoot, "src", "main", "resources");
		assertThatFileExists(appModuleRoot, "pom.xml");
		assertThatFileExists(appModuleRoot, "Dockerfile");
		assertThatFileExists(appModuleRoot, "mvnw");

		if (sparkerOptions.hasOption("flyway")) {
			assertThatFolderExists(appModuleRoot, "src", "main", "resources", "db", "migration");
			assertThatFileExists(appModuleRoot, "src", "main", "resources", "db", "migration", "V1__init.sql");
		}
	}

	private void assertWebModuleFiles(String rootFolder) {
		assertThatFolderExists(rootFolder, "project.web");
		assertThatFileExists(rootFolder, "project.web", "pom.xml");
		assertThatFolderDoesNotExist(rootFolder, "project.web", ".git");
		assertThatFolderExists(rootFolder, "project.api");
		assertThatFileExists(rootFolder, "project.api", "pom.xml");
	}

	private void assertThatBuildWorksCorrectly(SparkerOptions options) throws IOException, InterruptedException {
		System.out.println("Testing build of generated Project...");
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.redirectErrorStream(true);
		processBuilder.command("bash",
				"-c",
				"cd " + this.testFolderPath + "/" + options.getProjectName() + "&& " + "mvn clean install");
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		int exitVal = process.waitFor();
		if (exitVal == 0) {
			System.out.println("Success!");
		}
		assertEquals(0, exitVal);
	}

	@Test
	public void printHelpWorksCorrectly() {

		Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(HelpCommand.class);
		MemoryLogAppender memoryAppender = new MemoryLogAppender();
		memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		logger.setLevel(Level.DEBUG);
		logger.addAppender(memoryAppender);
		memoryAppender.start();

		SparkerOptions options = createSparkOptions();
		final PrintStream standardOutputStream = new PrintStream(System.out);
		final ByteArrayOutputStream outputStream = redirectOutput();
		sparkerService.printHelp(options);
		assertThatHelpIsPrinted(outputStream);
		System.setOut(standardOutputStream);
	}

	private ByteArrayOutputStream redirectOutput() {
		final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		return outContent;
	}

	private void assertThatHelpIsPrinted(ByteArrayOutputStream outputStream) {
		assertThat(outputStream.toString(), is("usage: java -jar sparker-X.X.X-SNAPSHOT.jar\n"
				+ "    --artifactId <arg>    The artifact id.\n"
				+ "    --basePath <arg>      The path where the project should be created.\n"
				+ "    --flyway              Generates files for flyway.\n"
				+ "    --groupId <arg>       The group id complying with the maven naming\n"
				+ "                          conventions.\n"
				+ "    --help                Shows the help dialog.\n"
				+ "    --language <arg>      The language of the project. Can be java or\n"
				+ "                          kotlin.\n"
				+ "    --mainClass <arg>     The desired name of the main class.\n"
				+ "    --projectName <arg>   The name of the project.\n"
				+ "    --runAsServer <arg>   Flag to run this application as a server.\n"));
	}

	@Test
	public void createParentModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkerOptions options = createSparkOptions();
		sparkerService.createParentModule(options);
		assertThatFolderExists("project");
		assertThatFileExists("project", "pom.xml");
		assertParentModuleFiles(options);
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project", "main_pom");
	}

	@Test
	public void createAppModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkerOptions options = createSparkOptions();
		sparkerService.createAppModule(options);
		assertAppModuleFiles(options);
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.app", "app_pom");
	}

	@Test
	public void createAPIModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkerOptions options = createSparkOptions();
		sparkerService.createAPIModule(options);
		assertThatFolderExists("project", "project.api");
		assertThatFileExists("project", "project.api", "pom.xml");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.api", "api_pom");
	}

	@Test
	public void createWebModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkerOptions options = createSparkOptions();
		sparkerService.createWebModule(options);
		assertThatFolderExists("project", "project.web");
		assertThatFileExists("project", "project.web", "pom.xml");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.web", "web_pom");
	}

	private void assertGeneratedPomFileIsValid(String generatedPomLocation, String comparisonFile) {
		DifferenceEngine diff = new DOMDifferenceEngine();

		Source generated = Input.fromFile(Paths.get(generatedPomLocation, "pom.xml").toFile()).build();
		Source control = Input.fromFile("src/test/resources/comparison/" + comparisonFile + ".xml").build();
		diff.addDifferenceListener((comparison, outcome) -> Assert.fail("found a difference: " + comparison));
		diff.compare(control, generated);
	}

	private SparkerOptions createSparkOptions() {
		return new SparkerOptions()
				.setBasePath(testFolderPath)
				.setProjectName("project")
				.setGroupId("com.trent.test")
				.setFlyWay(true)
				.setArtifactId("project");
	}

}
