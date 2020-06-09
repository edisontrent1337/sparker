package com.trent.sparker.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.trent.sparker.SparkerApplication;
import com.trent.sparker.service.commands.HelpCommand;
import com.trent.sparker.support.MemoryLogAppender;
import javax.xml.transform.Source;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparkerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SparkerServiceTest {

	@Autowired
	private SparkerService sparkerService;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	private Path testFolderPath;

	public SparkerServiceTest() {
	}

	@Before
	public void setUp() {
		this.testFolderPath = testFolder.getRoot().toPath();
		sparkerService.setBasePath(testFolderPath);
	}

	@Test
	public void createProjectWorksCorrectly() throws IOException, InterruptedException {

		SparkerOptions options = createSparkOptions();
		sparkerService.create(options);
		assertThatFolderExists("project");
		assertThatFileExists("project", "pom.xml");

		assertThatFolderExists("project", "project.app");
		assertThatFileExists("project", "project.app", "pom.xml");
		assertThatFolderExists("project", "project.ui");
		assertThatFileExists("project", "project.ui", "pom.xml");
		assertThatFolderExists("project", "project.api");
		assertThatFileExists("project", "project.api", "pom.xml");

		String testFolder = testFolderPath.toString();
		assertGeneratedPomFileIsValid(testFolder + "/project", "main_pom");
		assertGeneratedPomFileIsValid(testFolder + "/project/project.app", "app_pom");
		assertGeneratedPomFileIsValid(testFolder + "/project/project.api", "api_pom");
		assertGeneratedPomFileIsValid(testFolder + "/project/project.ui", "ui_pom");

		System.out.println("Testing build...");
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
				+ "    --groupId <arg>       The group id complying with the maven naming\n"
				+ "                          conventions.\n"
				+ "    --help <arg>          Shows the help dialog.\n"
				+ "    --language <arg>      The language of the project. Can be java or\n"
				+ "                          kotlin.\n"
				+ "    --mainClass <arg>     The desired name of the main class.\n"
				+ "    --projectName <arg>   The name of the project.\n"
				+ "    --runAsServer <arg>   Flag to run this application as a server.\n"));
	}

	@Test
	public void createParentModuleWorksCorrectly() throws IOException {
		SparkerOptions options = createSparkOptions();
		sparkerService.createParentModule(options);
		assertThatFolderExists("project");
		assertThatFileExists("project", "pom.xml");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project", "main_pom");
	}

	@Test
	public void createAppModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkerOptions options = createSparkOptions();
		sparkerService.createAppModule(options);
		assertThatFolderExists("project", "project.app");
		assertThatFileExists("project", "project.app", "pom.xml");
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
	public void createUIModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkerOptions options = createSparkOptions();
		sparkerService.createUIModule(options);
		assertThatFolderExists("project", "project.ui");
		assertThatFileExists("project", "project.ui", "pom.xml");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.ui", "ui_pom");
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
				.setArtifactId("project");
	}

	private void assertThatFileExists(String... pathArguments) {
		Path constructedPath = Paths.get(testFolderPath.toString(), pathArguments);
		assertTrue("The file " + constructedPath + " does not exist", constructedPath.toFile().exists());
	}

	private void assertThatFolderExists(String... pathArguments) {
		Path constructedPath = Paths.get(testFolderPath.toString(), pathArguments);
		assertTrue("The folder " + constructedPath + " does not exist", constructedPath.toFile().exists());
		assertTrue("The file " + constructedPath + " is not a folder.", constructedPath.toFile().isDirectory());
	}

}
