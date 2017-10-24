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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

    private static final String CLI_USAGE = "java -jar XIVStats-Gatherer-Java.jar [-abimDP] -s startid -f finishid [-d database-name] [-u database-user] [-p database-user-password] [-U database-url] [-t threads]";
    private static final Logger LOG = LoggerFactory.getLogger(Console.class);

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
            ApplicationConfig config = buildConfig(setupOptions(), args);

            GathererController gatherer = prepareGatherer(config, args);

            if(gatherer != null) {
                gatherer.run();
            }
        } catch(ParseException pEx) {
            new HelpFormatter().printHelp(CLI_USAGE, setupOptions());
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
    protected ApplicationConfig buildConfig(final Options options, final String... args) throws ParseException, SAXException, IOException,
                                                                                         ParserConfigurationException {
        ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                       .loadXMLConfiguration()
                                                       .loadCommandLineConfiguration(options, args)
                                                       .getConfiguration();

        return config;
    }

    protected GathererController prepareGatherer(final ApplicationConfig config, final String... args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(setupOptions(), args);
        GathererController controller = null;
        // Check for Help flag
        if(cmd.hasOption("h")) {
            new HelpFormatter().printHelp(CLI_USAGE, setupOptions());
        } else {
            controller = new GathererController(config);
        }
        return controller;
    }

    /**
     * Establish the possible options (flags) to run the program with.
     * 
     * @return the set of command line options.
     */
    public static Options setupOptions() {
        // create Options object
        Options options = new Options();

        // Add options
        Option optStart = Option.builder("s").longOpt("start").argName("start-id").hasArg().numberOfArgs(1).required()
                                .desc("the character id to start gatherer run from (inclusive)").required().build();
        Option optFinish = Option.builder("f").longOpt("finish").argName("end-id").hasArg().numberOfArgs(1).required()
                                 .desc("the character id to conclude gather run at (inclusive)").required().build();
        Option optHelp = Option.builder("h").longOpt("help").desc("display this help message").build();
        Option optStoreMinions = Option.builder("P").longOpt("store-minions")
                                       .desc("store minion data set for each player into the database").build();
        Option optStoreMounts = Option.builder("m").longOpt("store-mounts").desc("store mount data set for each player into the database")
                                      .build();
        Option optStoreProgression = Option.builder("b").longOpt("do-not-store-progress")
                                           .desc("do not store boolean data indicating player progress").build();
        Option optURL = Option.builder("U").longOpt("url").argName("database-server-url").hasArg().numberOfArgs(1)
                              .desc("the database url of the database server to connect to e.g. mysql://localhost:3306").build();
        Option optDB = Option.builder("d").longOpt("database").argName("database-name").hasArg().numberOfArgs(1).desc("database name")
                             .build();
        Option optUser = Option.builder("u").longOpt("user").argName("database-user").hasArg().numberOfArgs(1).desc("database user")
                               .build();
        Option optPassword = Option.builder("p").longOpt("password").argName("database-user-password").hasArg().numberOfArgs(1)
                                   .desc("database user password").build();
        Option optThreads = Option.builder("t").longOpt("threads").argName("no-threads").hasArg().numberOfArgs(1)
                                  .desc("number of gatherer threads to run").build();
        Option optStoreActive = Option.builder("a").longOpt("do-not-store-activity")
                                      .desc("do not store boolean data indicating player activity in last 30 days").build();
        Option optStoreDate = Option.builder("D").longOpt("do-not-store-date").desc("do not store Date of last player activity").build();
        Option optIgnoreSSLVerify = Option.builder("i").longOpt("ignore-ssl-verification")
                                          .desc("Supress/ignore MySQL SSL verification warnings").build();

        // Add each option to the options object
        options.addOption(optStart);
        options.addOption(optFinish);
        options.addOption(optStoreMinions);
        options.addOption(optStoreMounts);
        options.addOption(optStoreProgression);
        options.addOption(optDB);
        options.addOption(optUser);
        options.addOption(optPassword);
        options.addOption(optThreads);
        options.addOption(optURL);
        options.addOption(optHelp);
        options.addOption(optStoreActive);
        options.addOption(optStoreDate);
        options.addOption(optIgnoreSSLVerify);

        return options;
    }

}
