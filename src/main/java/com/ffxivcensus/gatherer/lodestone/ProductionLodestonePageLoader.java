package com.ffxivcensus.gatherer.lodestone;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loadestone Page Loader that works with the live EU lodestone,
 * 
 * @author matthew.hillier
 */
public class ProductionLodestonePageLoader implements LodestonePageLoader {

    /** Logger */
    private static final Logger LOG = LoggerFactory.getLogger(ProductionLodestonePageLoader.class);
    /** URL fragment for Minions. */
    private static final String SECTION_MINIONS = "minion";
    /** URL fragment for Mounts. */
    private static final String SECTION_MOUNTS = "mount";

    /**
     * Base URL used to fetch character data for.
     * Default to {@value}}
     */
    private String baseUrl = "http://eu.finalfantasyxiv.com/lodestone/character/%d/";

    /**
     * Fetches the given Character {@link Document} from the Lodestone.
     * 
     * @param characterId
     * @return A Jsoup Document object of the page.
     * @throws IOException
     * @throws InterruptedException
     * @throws CharacterDeletedException When the server returns a 404 response, a CharacterDeletedException will be thrown by this method.
     */
    @Override
    public Document getCharacterPage(final int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        return getPage(baseUrl, characterId, 1);
    }

    @Override
    public Document getMinionPage(int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        return getPage(baseUrl + SECTION_MINIONS, characterId, 1);
    }

    @Override
    public Document getMountPage(int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        return getPage(baseUrl + SECTION_MOUNTS, characterId, 1);
    }

    @Override
    public Document getTooltipPage(String href) throws IOException, InterruptedException {
        Document doc;
        try {
            doc = Jsoup.connect(href).timeout(5000).get();
        } catch(HttpStatusException httpe) {
            switch (httpe.getStatusCode()) {
                case 429:
                    // Generate random number 1->20*attempt no and sleep for it
                    Random rand = new Random();
                    int randomNum = rand.nextInt(5);
                    LOG.trace("Experiencing rate limiting (HTTP 429) while fetching tooltip, waiting " + randomNum + "ms then retrying...");
                    TimeUnit.MILLISECONDS.sleep(randomNum);
                    doc = getTooltipPage(href);
                    break;
                default:
                    throw new IOException("Unexpected HTTP Status Code: " + httpe.getStatusCode(), httpe);
            }
        }
        return doc;
    }

    private Document getPage(final String pageUrl, final int characterId, final int attempt) throws IOException, InterruptedException,
                                                                                             CharacterDeletedException {
        Document doc;

        String url = String.format(pageUrl, characterId);

        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch(HttpStatusException httpe) {
            switch (httpe.getStatusCode()) {
                case 429:
                    // Generate random number 1->20*attempt no and sleep for it
                    Random rand = new Random();
                    int max = attempt * 20;
                    int min = (attempt - 1) + 1;
                    int randomNum = rand.nextInt(max - min + 1) + min;
                    LOG.trace("Experiencing rate limiting (HTTP 429) while fetching id " + characterId + " (attempt " + attempt
                              + "), waiting " + randomNum + "ms then retrying...");
                    TimeUnit.MILLISECONDS.sleep(randomNum);
                    doc = getPage(url, characterId, attempt);
                    break;
                case HttpStatus.SC_NOT_FOUND:
                    LOG.info("Character {} does not exist. (404)", characterId);
                    throw new CharacterDeletedException();
                default:
                    throw new IOException("Unexpected HTTP Status Code: " + httpe.getStatusCode(), httpe);
            }
        }

        return doc;
    }

}
