package com.trent.sparker.service;

import com.trent.sparker.SparkerApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DOMDifferenceEngine;
import org.xmlunit.diff.DifferenceEngine;

import javax.xml.transform.Source;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparkerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SparkServiceTest {

	@Autowired
	private SparkService sparkService;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	private Path testFolderPath;

	public SparkServiceTest() {
	}

	@Before
	public void setUp() {
		this.testFolderPath = testFolder.getRoot().toPath();
		sparkService.setBasePath(testFolderPath);
	}

	@Test
	public void createProjectWorksCorrectly() throws IOException, InterruptedException {
		SparkOptions options = createSparkOptions();
		sparkService.create(options);
		assertThatFolderExists("project");
		assertThatFileExists("project", "pom.xml");

		assertThatFolderExists("project", "project.app");
		assertThatFileExists("project", "project.app", "pom.xml");
		assertThatFolderExists("project", "project.ui");
		assertThatFileExists("project", "project.ui", "pom.xml");
		assertThatFolderExists("project", "project.api");
		assertThatFileExists("project", "project.api", "pom.xml");

		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project", "main_pom");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.app", "app_pom");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.api", "api_pom");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.ui", "ui_pom");

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("bash", "-c", "cd " + this.testFolderPath + "/" + options.getProjectName() + "&& " + "mvn clean install");
		StringBuilder output = new StringBuilder();
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

		String line;
		while ((line = reader.readLine()) != null) {
			output.append(line).append("\n");
		}

		int exitVal = process.waitFor();
		if (exitVal == 0) {
			System.out.println("Success!");
		}
		System.out.println(output);
		assertEquals(0, exitVal);
	}


	@Test
	public void createParentModuleWorksCorrectly() throws IOException {
		SparkOptions options = createSparkOptions();
		sparkService.createParentModule(options);
		assertThatFolderExists("project");
		assertThatFileExists("project", "pom.xml");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project", "main_pom");
	}

	@Test
	public void createAppModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkOptions options = createSparkOptions();
		sparkService.createAppModule(options);
		assertThatFolderExists("project", "project.app");
		assertThatFileExists("project", "project.app", "pom.xml");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.app", "app_pom");
	}

	@Test
	public void createAPIModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkOptions options = createSparkOptions();
		sparkService.createAPIModule(options);
		assertThatFolderExists("project", "project.api");
		assertThatFileExists("project", "project.api", "pom.xml");
		assertGeneratedPomFileIsValid(testFolderPath.toString() + "/project/project.api", "api_pom");
	}

	@Test
	public void createUIModuleWorksCorrectly() throws IOException, InterruptedException {
		SparkOptions options = createSparkOptions();
		sparkService.createUIModule(options);
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

	private SparkOptions createSparkOptions() {
		return new SparkOptions()
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