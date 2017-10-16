package com.ffxivcensus.gatherer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.config.ConfigurationBuilder;

/**
 * Class to run instance of GatherController from the CLI.
 * 
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.GathererController
 * @since v1.0
 */
public class Console {
	
	private static final Logger LOG = LoggerFactory.getLogger(Console.class);

    public static void main(final String[] args) {
        run(args);
    }

    /**
     * Run the program provided a set of arguments.
     * 
     * @param args arguments to supply to the system.
     * @return the gatherer controller built up.
     */
    public static GathererController run(final String[] args) {
        // create Options object
        Options options = setupOptions();

        // Declare usage string
        String usage = "java -jar XIVStats-Gatherer-Java.jar [-abimDP] -s startid -f finishid [-d database-name] [-u database-user] [-p database-user-password] [-U database-url] [-t threads]";
        HelpFormatter formatter = new HelpFormatter();
        
        GathererController gatherer = null;

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            ApplicationConfig config = ConfigurationBuilder.createBuilder()
                                                           .loadXMLConfiguration()
                                                           .loadCommandLineConfiguration(options, args)
                                                           .getConfiguration();

            // Help flag
            if(cmd.hasOption("h")) {
                formatter.printHelp(usage, options);
            } else {
                gatherer = new GathererController(config);
                gatherer.run();
            }

            return gatherer;
        } catch(ParseException pEx) {
            formatter.printHelp(usage, options);
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return gatherer;
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
