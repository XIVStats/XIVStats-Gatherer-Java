package com.ffxivcensus.gatherer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffxivcensus.gatherer.config.ApplicationConfig;
import com.ffxivcensus.gatherer.player.PlayerBean;
import com.ffxivcensus.gatherer.player.PlayerBeanRepository;
import com.ffxivcensus.gatherer.player.PlayerBuilder;

/**
 * GathererController class of character gathering program. This class makes calls to fetch records from the lodestone, and then
 * subsequently writes them to the database. It also specifies the parameters to run the program with.
 *
 * @author Peter Reid
 * @since v1.0
 * @see PlayerBuilder
 * @see Gatherer
 */
@Service
public class GathererController {

    /** Defines the maximum number of characters to gather in a single iteration. **/
    private static final int GATHERING_ITERATON_MAX = 1000;
    private static final Logger LOG = LoggerFactory.getLogger(GathererController.class);
    private ApplicationConfig appConfig;
    private final GathererFactory gathererFactory;
    final PlayerBeanRepository playerRepository;
    /**
     * List of playable realms (used when splitting tables).
     */
    public final static String[] REALMS = {"Adamantoise", "Aegis", "Alexander", "Anima", "Asura", "Atomos", "Bahamut", "Balmung",
                                           "Behemoth", "Belias", "Brynhildr", "Cactuar", "Carbuncle", "Cerberus", "Chocobo", "Coeurl",
                                           "Diabolos", "Durandal", "Excalibur", "Exodus", "Faerie", "Famfrit", "Fenrir", "Garuda",
                                           "Gilgamesh", "Goblin", "Gungnir", "Hades", "Hyperion", "Ifrit", "Ixion", "Jenova", "Kujata",
                                           "Lamia", "Leviathan", "Lich", "Louisoix", "Malboro", "Mandragora", "Masamune", "Mateus",
                                           "Midgardsormr", "Moogle", "Odin", "Omega", "Pandaemonium", "Phoenix", "Ragnarok", "Ramuh",
                                           "Ridill", "Sargatanas", "Shinryu", "Shiva", "Siren", "Tiamat", "Titan", "Tonberry", "Typhon",
                                           "Ultima", "Ultros", "Unicorn", "Valefor", "Yojimbo", "Zalera", "Zeromus", "Zodiark"};

    /**
     * Constructs a new {@link GathererController} and configures with the provided {@link ApplicationConfig}.
     * 
     * @param config Configuration Bean
     */
    public GathererController(@Autowired final ApplicationConfig config, @Autowired final GathererFactory gathererFactory,
                              @Autowired final PlayerBeanRepository playerRepository) {
        this.appConfig = config;
        this.gathererFactory = gathererFactory;
        this.playerRepository = playerRepository;
    }

    /**
     * Start the gatherer controller instance up.
     *
     * @throws Exception Exception thrown if system is incorrectly configured.
     */
    public void run() throws Exception {
        // Store start time
        long startTime = System.currentTimeMillis();

        // If user attempts to exceed the maximum no. of threads - overwrite their input and set to MAX_THREADS
        if(appConfig.getThreadLimit() > ApplicationConfig.MAX_THREADS) {
            appConfig.setThreadLimit(ApplicationConfig.MAX_THREADS);
        }

        if(!isConfigured()) { // If not configured
            throw new Exception("Program not (correctly) configured");
        } else { // Else configured correctly
            try {
                if(appConfig.getStartId() > appConfig.getEndId()) {
                    LOG.error("Error: The finish id argument needs to be greater than the start id argument");
                } else { // Else pass values into poll method
                    LOG.info("Starting parse of range " + appConfig.getStartId() + " to " + appConfig.getEndId() + " using "
                             + appConfig.getThreadLimit() + " threads");
                    gatherCharacters(appConfig.getStartId(), appConfig.getEndId());
                    // Get current time
                    long endTime = System.currentTimeMillis();
                    long seconds = (endTime - startTime) / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    String time = days + " Days, " + hours % 24 + " hrs, " + minutes % 60 + " mins, " + seconds % 60 + " secs";
                    LOG.info("Run completed, " + ((appConfig.getEndId() - appConfig.getStartId()) + 1)
                             + " character IDs scanned in " + time + " (" + appConfig.getThreadLimit() + " threads)");

                }

            } catch(Exception ex) {
                throw new Exception(ex);
            }
        }
    }

    /**
     * Determine whether the instance is correctly configured.
     *
     * @return string containing warnings/errors in configuration.
     */
    public boolean isConfigured() {
        boolean configured = true;
        if(appConfig.getStartId() < 0) {
            LOG.error("Start ID must be configured to a positive numerical value");
            configured = false;
        }
        if(appConfig.getEndId() < 0) {
            LOG.error("End ID must be configured to a positive numerical value");
            configured = false;
        }
        return configured;
    }

    /**
     * Method to gather data for characters in specified range.
     */
    private void gatherCharacters(final int startId, final int finishId) {
        // Firstly, clean the top-end of the database
        LOG.debug("Cleaning top-end characters from the database");
        Integer highestValid = playerRepository.findTopByIdByCharacterStatusNotDeleted();
        // Delete everything higher than last known good player
        gatherCharacterRange(startId, finishId, 1);
    }

    /**
     * Recursive method to gather characters between the given start and finish ID's, up to a maximum of {@value #GATHERING_ITERATON_MAX}
     * (see {@link #GATHERING_ITERATON_MAX}) characters per iteration.
     * 
     * @param startId First ID to gather.
     * @param finishId Final ID to gather.
     * @param iteration Recursive iteration depth.
     */
    private void gatherCharacterRange(final int startId, final int finishId, final int iteration) {
        // Setup an executor service that respects the max thread limit
        ExecutorService executor = Executors.newFixedThreadPool(appConfig.getThreadLimit());

        // Set next ID
        int nextID = startId;

        while(nextID <= finishId && nextID <= (startId + GATHERING_ITERATON_MAX)) {
            Gatherer worker = gathererFactory.createGatherer();
            worker.setIteration(iteration);
            worker.setPlayerId(nextID);
            executor.execute(worker);

            nextID++;
        }

        executor.shutdown();
        while(!executor.isTerminated()) {
            try {
                // Wait patiently for the executor to finish off everything submitted
                executor.awaitTermination(30, TimeUnit.SECONDS);
            } catch(InterruptedException ie) {
                // Unless there's an interrupt, in which case shut down gracefully
                executor.shutdownNow();
            }
        }

        if(nextID < finishId) {
            gatherCharacterRange(nextID, finishId, iteration + 1);
        }
    }
}
