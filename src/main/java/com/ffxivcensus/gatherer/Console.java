package com.ffxivcensus.gatherer;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;

/**
 * Class to run instance of GatherController from the CLI.
 * 
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.GathererController
 * @since v1.0
 */
@SpringBootApplication
public class Console implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Console.class);
    @Autowired
    private ApplicationConfig config;

    public static void main(final String[] args) {
        SpringApplication.run(Console.class, args);
    }

    /**
     * Run the program provided a set of arguments.
     * 
     * @param args arguments to supply to the system.
     * @return the gatherer controller built up.
     */
    public void run(final String... args) {
        try {
            applyCommandLineOptions(config, args);

            GathererController gatherer = prepareGatherer(config, args);

            if(gatherer != null) {
                gatherer.run();
            }
        } catch(ParseException pEx) {
            new HelpFormatter().printHelp(CLIConstants.CLI_USAGE, CLIConstants.setupOptions());
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Returns a completed configuration object, including both config file and command line options.
     * 
     * @param options Available Command-Line options.
     * @param args Command-Line arguments.
     * @return
     * @throws ParseException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    protected ApplicationConfig applyCommandLineOptions(final ApplicationConfig config, final String... args) throws ParseException,
                                                                                                              SAXException, IOException,
                                                                                                              ParserConfigurationException {
        return ConfigurationBuilder.createBuilder(config)
                                   .loadCommandLineConfiguration(CLIConstants.setupOptions(), args)
                                   .getConfiguration();
    }

    protected GathererController prepareGatherer(final ApplicationConfig config, final String... args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(CLIConstants.setupOptions(), args);
        GathererController controller = null;
        // Check for Help flag
        if(cmd.hasOption("h")) {
            new HelpFormatter().printHelp(CLIConstants.CLI_USAGE, CLIConstants.setupOptions());
        } else {
            controller = new GathererController(config);
        }
        return controller;
    }

}
