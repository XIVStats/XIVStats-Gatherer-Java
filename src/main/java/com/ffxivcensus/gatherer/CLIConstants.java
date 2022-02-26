package com.ffxivcensus.gatherer;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Class encapsulating all of the CLI constants.
 *
 * @author matthew.hillier
 */
public class CLIConstants {
    public static final String CLI_USAGE = "java -jar XIVStats-Gatherer-Java.jar [-i] -s startid [-f finishid] [-a autostop-id] [-g autostop-gap] [-d database-name] [-u database-user] [-p database-user-password] [-U database-url] [-t threads] [-l label]";

    /**
     * Private constructor as this should never me initiated.
     */
    private CLIConstants() {
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
        Option optStart = Option.builder("s").longOpt("start").argName("start-id").hasArg().numberOfArgs(1)
                                .desc("the character id to start gatherer run from (inclusive)").required().build();
        Option optFinish = Option.builder("f").longOpt("finish").argName("end-id").hasArg().numberOfArgs(1)
                                 .desc("the character id to conclude gather run at (inclusive)").build();
        Option optAutoStopLimit = Option.builder("a").longOpt("autostopfrom").argName("autostop-from").hasArg().numberOfArgs(1)
                                        .desc("the lowest character id to allow auto-stop to happen").build();
        Option optAutoStopGap = Option.builder("g").longOpt("autostopgap").argName("autostop-gap").hasArg().numberOfArgs(1)
                                       .desc("the number of continuous invalid characters to trigger auto-stopping").build();
        Option optHelp = Option.builder("h").longOpt("help").desc("display this help message").build();
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
        Option optIgnoreSSLVerify = Option.builder("i").longOpt("ignore-ssl-verification")
                                          .desc("Supress/ignore MySQL SSL verification warnings").build();
		Option optDataLabel = Option.builder("l").longOpt("label").argName("label").hasArg().numberOfArgs(1).required().desc("Label to apply to aggregate table rows for this run").build();

        // Add each option to the options object
        options.addOption(optStart);
        options.addOption(optFinish);
        options.addOption(optAutoStopLimit);
        options.addOption(optAutoStopGap);
        options.addOption(optDB);
        options.addOption(optUser);
        options.addOption(optPassword);
        options.addOption(optThreads);
        options.addOption(optURL);
        options.addOption(optHelp);
        options.addOption(optIgnoreSSLVerify);
        options.addOption(optDataLabel);

        return options;
    }
}
