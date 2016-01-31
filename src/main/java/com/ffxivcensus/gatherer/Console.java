package com.ffxivcensus.gatherer;

import org.apache.commons.cli.*;

/**
 * Class to run instance of GatherController from the CLI.
 * @author Peter Reid
 * @see com.ffxivcensus.gatherer.GathererController
 * @since v1.0
 */
public class Console {

    public static void main(String[] args) {

        // create Options object
        Options options = setupOptions();

        //Declare usage string
        String usage = "java -jar XIVStats-Gatherer-Java.jar [-bmqDPS] -s startid -f finishid [-d database-name] [-u database-user] [-p database-user-password] [-U database-url] [-T table] [-t threads]";
        HelpFormatter formatter = new HelpFormatter();

        try{
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse( options, args);

            int startID = -1;
            int endID = -1;

            //Start id flag
            if(cmd.hasOption("s")){
                startID =  Integer.parseInt(cmd.getOptionValue("s"));
            }

            //Finish id flag
            if (cmd.hasOption("f")){
                endID = Integer.parseInt(cmd.getOptionValue("f"));
            }

            GathererController gatherer = new GathererController(startID,endID);

            //Help flag
            if (cmd.hasOption("h")){
                formatter.printHelp(usage,options);
            }

            //Store minions flag value
            gatherer.setStoreMinions(cmd.hasOption("P"));

            //Store mounts flag value
            gatherer.setStoreMounts(cmd.hasOption("m"));

            //Store progression
            gatherer.setStoreProgression(cmd.hasOption("b"));

            //Database URL
            if(cmd.hasOption("d") && cmd.hasOption("U")){
                gatherer.setDbUrl("jdbc:" + cmd.getOptionValue("U") + "/" + cmd.getOptionValue("d"));
            } else if (cmd.hasOption("d")){
                gatherer.setDbUrl("jdbc:mysql://localhost:3306/" + cmd.getOptionValue("d"));
            }

            //Database user
            if(cmd.hasOption("u")){
                gatherer.setDbUser(cmd.getOptionValue("u"));
            }

            //Database password
            if(cmd.hasOption("p")){
                gatherer.setDbPassword(cmd.getOptionValue("p"));
            }

            //Program threads
            if(cmd.hasOption("t")){
                gatherer.setThreadLimit(Integer.parseInt(cmd.getOptionValue("t")));
            }

            //Database table
            if(cmd.hasOption("T")){
                gatherer.setTableName(cmd.getOptionValue("T"));
            }

            //Split tables
            gatherer.setSplitTables(cmd.hasOption("S"));

            //Verbose mode
            if(cmd.hasOption("q")){
                gatherer.setVerbose(false);
            } else {
                gatherer.setVerbose(true);
            }

            //Debug mode
            if(cmd.hasOption("D")){
                gatherer.setPrintDuds(true);
            } else{
                gatherer.setPrintDuds(false);
            }

            gatherer.run();
        }
        catch (ParseException pEx) {
            formatter.printHelp(usage, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Establish the possible options (flags) to run the program with.
     * @return the set of command line options.
     */
    public static Options setupOptions(){
        // create Options object
        Options options = new Options();

        //Add options
        Option optStart = Option.builder("s").longOpt("start").argName("start-id").hasArg().numberOfArgs(1).required().desc("the character id to start gatherer run from (inclusive)").required().build();
        Option optFinish = Option.builder("f").longOpt("finish").argName("end-id").hasArg().numberOfArgs(1).required().desc("the character id to conclude gather run at (inclusive)").required().build();
        Option optHelp = Option.builder("h").longOpt("help").desc("display this help message").build();
        Option optStoreMinions = Option.builder("P").longOpt("store-minions").desc("store minion data set for each player into the database").build();
        Option optStoreMounts = Option.builder("m").longOpt("store-mounts").desc("store mount data set for each player into the database").build();
        Option optStoreProgression = Option.builder("b").longOpt("do-not-store-progress").desc("do not store boolean data indicating player progress").build();
        Option optURL = Option.builder("U").longOpt("url").argName("database-server-url").hasArg().numberOfArgs(1).desc("the database url of the database server to connect to e.g. mysql://localhost:3306").build();
        Option optDB = Option.builder("d").longOpt("database").argName("database-name").hasArg().numberOfArgs(1).desc("database name").build();
        Option optUser = Option.builder("u").longOpt("user").argName("database-user").hasArg().numberOfArgs(1).desc("database user").build();
        Option optPassword = Option.builder("p").longOpt("password").argName("database-user-password").hasArg().numberOfArgs(1).desc("database user password").build();
        Option optThreads = Option.builder("t").longOpt("threads").argName("no-threads").hasArg().numberOfArgs(1).desc("number of gatherer threads to run").build();
        Option optTable = Option.builder("T").longOpt("table").hasArg().numberOfArgs(1).argName("table").desc("the table to write records to").build();
        Option optSplitTable = Option.builder("S").longOpt("split-table").hasArg().numberOfArgs(1).argName("table-suffix").desc("split tblplayers into multiple tables").build();
        Option optVerbose = Option.builder("q").longOpt("quiet").desc("run program in quiet mode - no console output").build();
        Option optDebug = Option.builder("D").longOpt("debug").desc("run program in debug mode - full console output").build();

        //Add each option to the options object
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
        options.addOption(optTable);
        options.addOption(optSplitTable);
        options.addOption(optVerbose);
        options.addOption(optDebug);

        return options;
    }

}
