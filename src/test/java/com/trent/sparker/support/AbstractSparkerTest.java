package com.trent.sparker.support;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.trent.sparker.SparkerApplication;
import com.trent.sparker.service.SparkerService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparkerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractSparkerTest {

	@Autowired
	protected SparkerService sparkerService;

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();
	protected Path testFolderPath;

	@Before
	public void setUp() {
		this.testFolderPath = testFolder.getRoot().toPath();
	}

	 protected void assertThatFileExists(String... pathArguments) {
		Path constructedPath = Paths.get(testFolderPath.toString(), pathArguments);
		assertTrue("The file " + constructedPath + " does not exist", constructedPath.toFile().exists());
	}

	 protected void assertThatFolderExists(String... pathArguments) {
		Path constructedPath = Paths.get(testFolderPath.toString(), pathArguments);
		assertTrue("The folder " + constructedPath + " does not exist", constructedPath.toFile().exists());
		assertTrue("The file " + constructedPath + " is not a folder.", constructedPath.toFile().isDirectory());
	}

	 protected void assertThatFolderDoesNotExist(String... pathArguments) {
		Path constructedPath = Paths.get(testFolderPath.toString(), pathArguments);
		assertFalse("The folder " + constructedPath + " does exist but it should not.",
				constructedPath.toFile().exists());
	}

}