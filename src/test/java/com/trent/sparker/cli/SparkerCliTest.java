package com.trent.sparker.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.trent.sparker.SparkerApplication;
import com.trent.sparker.support.AbstractSparkerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SparkerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SparkerCliTest extends AbstractSparkerTest {

	@Test
	public void buildingProjectWithoutAnyArgumentsWorksAsExpected() throws IOException, InterruptedException {
		System.out.println("Running integration test.");
		runCommand("java -jar ./target/sparker-0.0.1-SNAPSHOT.jar "
				+ "--basePath " + this.testFolderPath.toString()
		);
	}

	@Test
	public void buildingProjectFromCommandLineWorksAsExpected() throws IOException, InterruptedException {
		System.out.println("Running integration test.");
		runCommand("java -jar ./target/sparker-0.0.1-SNAPSHOT.jar "
				+ "--projectName myproject "
				+ "--artifactId myartifact "
				+ "--groupId synexion "
				+ "--mainClass MyMainClass "
				+ "--flyway "
				+ "--basePath " + this.testFolderPath.toString()
		);
		assertThatFileExists("myproject",
				"myproject.app",
				"src",
				"main",
				"resources",
				"db",
				"migration",
				"V1__init.sql");
	}

	private void runCommand(String command) throws IOException, InterruptedException {
		String presentWorkingDirectory = System.getProperty("user.dir");
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("bash", "-c", "cd " + presentWorkingDirectory + " && " + command);
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
		assertThat(exitVal, is(equalTo(0)));
	}
}
