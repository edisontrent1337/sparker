package com.trent.sparker.service;

import com.trent.sparker.SparkerApplication;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

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
	public void createProjectWorksCorrectly() {
		SparkOptions options = new SparkOptions()
				.setBasePath(testFolderPath)
				.setProjectName("project")
				.setGroupId("com.trent.test")
				.setArtifactId("project");
		sparkService.create(options);
		assertThatFolderExists("project");
		assertThatFolderExists("project", "project.app");
		assertThatFileExists("project", "project.app", "pom.xml");
		assertThatFolderExists("project", "project.ui");
		assertThatFileExists("project", "project.ui", "pom.xml");
		assertThatFolderExists("project", "project.api");
		assertThatFileExists("project", "project.api", "api.yml");
		assertThatFileExists("project", "project.api", "pom.xml");
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