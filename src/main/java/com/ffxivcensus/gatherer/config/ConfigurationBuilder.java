package com.ffxivcensus.gatherer.config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ffxivcensus.gatherer.GathererController;

/**
 * Builder for the Gatherer configuration options.
 * By default, will create a configuration bean that holds the default options for running the Gatherer against a local mysql database.
 * Once created, the caller may choose to call any of the other configuration layers to add/layer additional configuration to the bean.
 * The preferred order of configuration would be:
 * <ol>
 * <li>Default configuration - Hard coded</li>
 * <li>Application configuration - config.xml</li>
 * <li>Runtime configuration - command-line options</li>
 * </ol>
 * Once configuration has been completed, fetch the {@link ApplicationConfig} bean using the {@link #getConfiguration()} method and pass
 * into the {@link GathererController}.
 * 
 * @author matthew.hillier
 */
public class ConfigurationBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationBuilder.class);
    private ApplicationConfig configuration;

    /**
     * Returns a new {@link ConfigurationBuilder} object with a pre-configured {@link ApplicationConfig} bean in it's default state.
     * 
     * @return New {@link ConfigurationBuilder} instance ready to use.
     */
    public static ConfigurationBuilder createBuilder() {
        return new ConfigurationBuilder(new ApplicationConfig());
    }

    /**
     * Returns a new {@link ConfigurationBuilder} object ready to work with the given {@link ApplicationConfig} bean.
     * 
     * @return New {@link ConfigurationBuilder} instance ready to use.
     */
    public static ConfigurationBuilder createBuilder(ApplicationConfig existingConfig) {
        return new ConfigurationBuilder(existingConfig);
    }

    /**
     * Creates a new {@link ConfigurationBuilder} instance, passing in the given {@link ApplicationConfiguration} object.
     * This is private to ensure that other callers cannot intercept and replace the {@link ApplicationConfig} bean by accident.
     * 
     * @param configuration {@link ApplicationConfig} bean to be used with the new instance of the {@link ConfigurationBuilder}.
     */
    public ConfigurationBuilder(ApplicationConfig configuration) {
        this.configuration = configuration;
    }

    /**
     * Load the application configuration from the config,xml file, assuming it can be found.
     *
     * @return New {@link ConfigurationBuilder} containing the updated Configuration bean and ready to be further configured.
     * @throws ParserConfigurationException Indicates a serious configuration error.
     * @throws IOException Indicates an error reading the file specified.
     * @throws SAXException Indicates an error parsing XML.
     */
    public ConfigurationBuilder loadXMLConfiguration() throws ParserConfigurationException, IOException, SAXException {
        // Set config file location
        File xmlFile = new File("config.xml");

        if(xmlFile.exists()) {
            // Initialize parsers
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // Parse the file
            Document doc = dBuilder.parse(xmlFile);

            // Read out db config
            NodeList nodesJDBC = doc.getElementsByTagName("jdbc");
            Element elementJDBC = (Element) nodesJDBC.item(0);

            configuration.setDbUrl(elementJDBC.getElementsByTagName("url").item(0).getTextContent());
            configuration.setDbName(elementJDBC.getElementsByTagName("database").item(0).getTextContent());
            configuration.setDbUser(elementJDBC.getElementsByTagName("username").item(0).getTextContent());
            configuration.setDbPassword(elementJDBC.getElementsByTagName("password").item(0).getTextContent());

            // Read out execution config
            NodeList nodesExecConf = doc.getElementsByTagName("execution");
            Element elementExecConf = (Element) nodesExecConf.item(0);
            configuration.setThreadLimit(Integer.parseInt(elementExecConf.getElementsByTagName("threads").item(0).getTextContent()));
        } else {
            LOG.error("Configuration: No config.xml file found. Failing over to defaults.");
        }

        return new ConfigurationBuilder(configuration);
    }

    /**
     * Sets configuration based on the input from the Command Line.
     * 
     * @param options Command Line Options presented to the User
     * @param args Arguments passed in from the Command Line
     * @return New {@link ConfigurationBuilder} containing the updated Configuration bean and ready to be further configured.
     * @throws ParseException May throw {@link ParseException} when reading the Command Line arguments
     */
    public ConfigurationBuilder loadCommandLineConfiguration(Options options, String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // Start id flag
        if(cmd.hasOption("s")) {
            configuration.setStartId(Integer.parseInt(cmd.getOptionValue("s")));
        }

        // Finish id flag
        if(cmd.hasOption("f")) {
            configuration.setEndId(Integer.parseInt(cmd.getOptionValue("f")));
        }

        // Store minions flag value
        configuration.setStoreMinions(cmd.hasOption("P"));

        // Store mounts flag value
        configuration.setStoreMounts(cmd.hasOption("m"));

        // Store progression
        configuration.setStoreProgression(!cmd.hasOption("b"));

        // Store whether player is active
        configuration.setStorePlayerActive(!cmd.hasOption("a"));

        // Store player active date
        configuration.setStoreActiveDate(!cmd.hasOption("D"));

        // Database URL
        if(cmd.hasOption("d") && cmd.hasOption("U")) {
            configuration.setDbUrl(cmd.getOptionValue("U"));
        }
        // Database Name
        if(cmd.hasOption("d")) {
            configuration.setDbName(cmd.getOptionValue("d"));
        }

        // Database user
        if(cmd.hasOption("u")) {
            configuration.setDbUser(cmd.getOptionValue("u"));
        }

        // Database password
        if(cmd.hasOption("p")) {
            configuration.setDbPassword(cmd.getOptionValue("p"));
        }

        configuration.setDbIgnoreSSLWarn(cmd.hasOption("i"));

        // Program threads
        if(cmd.hasOption("t")) {
            configuration.setThreadLimit(Integer.parseInt(cmd.getOptionValue("t")));
        }

        return new ConfigurationBuilder(configuration);
    }

    /**
     * Fetches the {@link ApplicationConfig} bean configured by this builder.
     * 
     * @return {@link ApplicationConfig} bean.
     */
    public ApplicationConfig getConfiguration() {
        return configuration;
    }

}
