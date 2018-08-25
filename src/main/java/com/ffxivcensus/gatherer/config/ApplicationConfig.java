package com.ffxivcensus.gatherer.config;

/**
 * Application configuration bean encapsulating all of the configuration options available for configuration.
 * By default, the following sensible defaults are set:
 * <dl>
 * <dt>{@link #dbUrl}</dt>
 * <dd>{@link #DEFAULT_DATABASE_HOST}</dd>
 * <dt>{@link #quiet}</dt>
 * <dd>true</dd>
 * <dt>{@link #storeProgression}</dt>
 * <dd>true</dt>
 * <dt>{@link #dbIgnoreSSLWarn}</dt>
 * <dd>true</dd>
 * </dl>
 * 
 * @author matthew.hillier
 */
public class ApplicationConfig {

    public static final String DEFAULT_CONFIG_FILE = "config.xml";
    public static final String DEFAULT_DATABASE_HOST = "mysql://localhost:3306";
    public static final String DEFAULT_DATABASE_NAME = "dbplayers";
    public static final String DEFAULT_TABLE_NAME = "tblplayers";

    /**
     * Safety limit for thread count - user cannot exceed this limit.
     */
    public static final int MAX_THREADS = 64;

    /////////////////////////
    // Database Configuration
    /////////////////////////

    /**
     * The JDBC URL of the database to modify
     */
    private String dbUrl = DEFAULT_DATABASE_HOST;
    /**
     * The Name of the Database to use
     */
    private String dbName = DEFAULT_DATABASE_NAME;
    /**
     * The Username of user of the SQL server user to use.
     */
    private String dbUser;
    /**
     * The password for the user, to use.
     */
    private String dbPassword;
    /**
     * Whether to ignore database SSL verification warnings
     */
    private boolean dbIgnoreSSLWarn = true;

    ////////////////////////
    // Process Configuration
    ////////////////////////

    /**
     * User-defined limit for thread count.
     */
    private int threadLimit = MAX_THREADS;

    //////////////////////////
    // Gathering Configuration
    //////////////////////////

    /**
     * The character ID to start the gatherer at.
     */
    private int startId = -1;
    /**
     * The character ID to end the gatherer at.
     */
    private int endId = Integer.MAX_VALUE;

    /////////////////////////////////
    // Database Configuration Methods
    /////////////////////////////////

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public boolean isDbIgnoreSSLWarn() {
        return dbIgnoreSSLWarn;
    }

    public void setDbIgnoreSSLWarn(boolean dbIgnoreSSLWarn) {
        this.dbIgnoreSSLWarn = dbIgnoreSSLWarn;
    }

    ////////////////////////
    // Process Configuration
    ////////////////////////

    public int getThreadLimit() {
        return threadLimit;
    }

    public void setThreadLimit(int threadLimit) {
        this.threadLimit = threadLimit;
    }

    //////////////////////////
    // Gathering Configuration
    //////////////////////////

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }
}