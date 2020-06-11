package com.trent.sparker;

import com.trent.sparker.service.SparkerOptions;
import com.trent.sparker.service.SparkerService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SparkerApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkerApplication.class);

	public static void main(String[] args) {

//		if (args.length == 0) {
//			LOGGER.error("Please provide options to use Sparker.");
//			System.exit(1);
//		}

		SparkerOptions options = new SparkerOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("runAsServer") && "true".equals(cmd.getOptionValue("runAsServer"))) {
				LOGGER.info("Running Sparker in server mode. This feature is not fully supported yet.");
				SpringApplication.run(SparkerApplication.class, args);
			} else {
				runAsCLITool(new SparkerOptions(cmd));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private static void runAsCLITool(SparkerOptions sparkerOptions) {
		LOGGER.info("Running Sparker as CLI tool.");
		SparkerService sparkerService = new SparkerService();
		if (sparkerOptions.hasOption("help")) {
			sparkerService.printHelp(sparkerOptions);
		} else {
			LOGGER.info(sparkerOptions.toString());
			sparkerService.create(sparkerOptions);
		}
	}
}
